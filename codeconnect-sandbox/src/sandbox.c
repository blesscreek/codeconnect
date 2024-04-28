#define _GNU_SOURCE

#include <unistd.h>
#include <fcntl.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/ptrace.h>
#include <sys/time.h>
#include <sys/resource.h>
#include <sys/wait.h>
#include <errno.h>
#include <pthread.h>
#include <signal.h>
#include <sys/utsname.h>
#include <sched.h>
#include <stdint.h>
#include <sys/mman.h>

#include "constants.h"
#include "utils.h"

extern struct Config runner_config;
extern struct Result runner_result;

static int result_pipes[2];

static int msg_pipes[2];

static pid_t box_pid;
static pid_t proxy_pid;

struct rlimit _rl;

struct killer_parameter
{
  pid_t pid;
  int timeout;
};

#define SET_LIMIT(FIELD,VALUE) \
	log_debug("set %s : %ld", #FIELD, VALUE); \
	_rl.rlim_cur = _rl.rlim_max = (rlim_t)(VALUE); \
	if (setrlimit(FIELD, &_rl)) \
		CHILD_ERROR_EXIT("set" #FIELD "failure");
void child_process() {
	int input_fd = -1;
	int output_fd = -1;
	int err_fd = -1;
	int null_fd = open("/dev/null", O_RDWR);

	if (runner_config.cpu_time_limit != RESOURCE_UNLIMITED) {
		SET_LIMIT(RLIMIT_CPU, (runner_config.cpu_time_limit + 1000)/ 1000);
	}
	if (runner_config.memory_limit != RESOURCE_UNLIMITED) {
		if (runner_config.memory_check_only == 0) {
			SET_LIMIT(RLIMIT_AS, runner_config.memory_limit * 1024 * 2);
		}
	}

	SET_LIMIT(RLIMIT_NOFILE, LIMITS_MAX_FD);
	SET_LIMIT(RLIMIT_FSIZE, LIMITS_MAX_OUTPUT);

	if (runner_config.stdin_file) {
		input_fd = open(runner_config.stdin_file, O_RDONLY | O_CREAT, 0700);
		if (input_fd != -1) {
			log_debug("open stdin_file");
			if (dup2(input_fd, STDIN_FILENO) == -1) {
				CHILD_ERROR_EXIT("input_fd dup error");
			}
		} else {
			CHILD_ERROR_EXIT("error open stdin_file");
		}
	} else {
		log_info("stdin_file is not set");
		if (runner_config.attach_stdin == 0) {
			log_info("redirected stdin to /dev/null");
			dup2(null_fd, STDIN_FILENO);
		} else {
			log_info("use stdin");
		}
	}

	if (runner_config.stdout_file)
  	{
	    output_fd = open(runner_config.stdout_file, O_WRONLY | O_CREAT | O_TRUNC, 0700);
	    if (output_fd != -1)
	    {
	      log_debug("open stdout_file");
	      if (dup2(output_fd, STDOUT_FILENO) == -1)
	      {
	        CHILD_ERROR_EXIT("output_fd dup error");
	      }
	    }
	    else
	    {
	      CHILD_ERROR_EXIT("error open stdout_file");
	    }
  	}
  	else
  	{
	    log_info("stdout_file is not set");
	    if (runner_config.attach_stdout == 0)
	    {
	      log_info("redirected stdout to /dev/null");
	      dup2(null_fd, STDOUT_FILENO);
	    }
	    else
	    {
	      log_info("use stdout");
	    }
  	}

  	if (runner_config.stderr_file)
  	{
	    err_fd = open(runner_config.stderr_file, O_WRONLY | O_CREAT | O_TRUNC, 0700);
	    if (err_fd != -1)
	    {

	      if (dup2(err_fd, STDERR_FILENO) == -1)
	      {
	        CHILD_ERROR_EXIT("err_fd");
	      }
	    }
    	else
    	{
      		CHILD_ERROR_EXIT("error open err_fd");
    	}
  	}
  	else
  	{
	    log_info("err_file is not set");
	    if (runner_config.attach_stderr == 0)
	    {
	      log_info("redirected stderr to /dev/null");
	      dup2(null_fd, STDERR_FILENO);
	    }
	    else
	    {
	      log_info("use stderr");
	    }
  	}

  	log_debug("exec %s",runner_config.cmd[0]);
  	execvp(runner_config.cmd[0], runner_config.cmd);
  	CHILD_ERROR_EXIT("exec cmd error");
}
void *timeout_killer(void *killer_para) {
  pid_t pid = ((struct killer_parameter *)killer_para)->pid;
  int timeout = ((struct killer_parameter *)killer_para)->timeout;
  if (pthread_detach(pthread_self()) != 0) {
		kill_pid(pid);
		return NULL;
	}
	if (sleep((unsigned int)((timeout + 500) / 1000)) == 0) {
		log_debug("timeout, kill user application");
		kill_pid(pid);
		return NULL;
	}
	if (kill_pid(pid) != 0) {
		return NULL;
	}
	return NULL;
}
void log_rusage(struct rusage* ru) {
  	log_debug("rusage: user time used tv_sec %ld s", ru->ru_utime.tv_sec);
  	log_debug("rusage: user time used tv_usec %ld us", ru->ru_utime.tv_usec);
  	log_debug("rusage: system time tv_sec %ld s", ru->ru_stime.tv_sec);
  	log_debug("rusage: system time tv_usec %ld us", ru->ru_stime.tv_usec);
  	log_debug("rusage: maximum resident set size %ld kb", ru->ru_maxrss);
  	log_debug("rusage: page reclaims %ld", ru->ru_minflt);
  	log_debug("rusage: page faults %ld", ru->ru_majflt);
  	log_debug("rusage: block input operations %ld", ru->ru_inblock);
  	log_debug("rusage: block output operations %ld", ru->ru_oublock);
  	log_debug("rusage: voluntary context switches %ld", ru->ru_nvcsw);
  	log_debug("rusage: involuntary context switches %ld", ru->ru_nivcsw);
}
void monitor(pid_t child_pid) {
	struct timeval start_time, end_time;
	gettimeofday(&start_time, NULL);
	pthread_t tid = 0;
	if (runner_config.real_time_limit != RESOURCE_UNLIMITED) {
		struct killer_parameter para =
            {
                .timeout = runner_config.real_time_limit,
                .pid = child_pid,
            };
		if (pthread_create(&tid, NULL, timeout_killer, (void *)(&para)) != 0) {
			kill_pid(child_pid);
			INTERNAL_ERROR_EXIT("pthread(timeout_killer) create error");
		}
	}
	int status;
	struct rusage ru;

	if (wait4(child_pid, &status, 0, &ru) == -1) {
		INTERNAL_ERROR_EXIT("wait4 error");
	}

	gettimeofday(&end_time, NULL);
	log_rusage(&ru);
	const struct timeval real_time_tv = {
		(end_time.tv_sec - start_time.tv_sec),
      	(end_time.tv_usec - start_time.tv_usec),
	};
  	runner_result.real_time_used = tv_to_ms(&real_time_tv);
  	runner_result.real_time_used_us = tv_to_us(&real_time_tv);
  	const struct timeval cpu_time_tv = {
      ((&ru.ru_utime)->tv_sec + (&ru.ru_stime)->tv_sec),
      ((&ru.ru_utime)->tv_usec + (&ru.ru_stime)->tv_usec),
  	};
	runner_result.cpu_time_used = tv_to_ms(&cpu_time_tv);
  	runner_result.cpu_time_used_us = tv_to_us(&cpu_time_tv);
	runner_result.memory_used = ru.ru_maxrss;
  	if (runner_config.real_time_limit != RESOURCE_UNLIMITED)
  	{
    	if (pthread_cancel(tid) != 0)
    	{
      		log_debug("cancel fail");
    	};
  	}
	if (WIFSIGNALED(status)) {
		runner_result.signal_code = WTERMSIG(status);
    	log_debug("child process exit abnormal, signal: %d, strsignal: %s", runner_result.signal_code, strsignal(runner_result.signal_code));
   		switch (runner_result.signal_code)
	    {
	    //程序发出的结束程序的信号
	    case SIGUSR1:
	      runner_result.status = SYSTEM_ERROR;
	      break;
	    //要执行的程序没有找到
	    case SIGNF:
	      runner_result.status = SYSTEM_ERROR;
	      runner_result.error_code = COMMAND_NOT_FOUND;
	      break;
	    //无效的内存访问 内存超限或运行是异常
	    case SIGSEGV:
	      if (runner_result.memory_used > runner_config.memory_limit)
	        runner_result.status = MEMORY_LIMIT_EXCEEDED;
	      else
	        runner_result.status = RUNTIME_ERROR;
	      break;
	    case SIGALRM:
	    case SIGXCPU:
	      runner_result.status = TIME_LIMIT_EXCEEDED;
	      break;
	    case SIGKILL:
	    default:
	      if (runner_config.cpu_time_limit != RESOURCE_UNLIMITED && runner_result.cpu_time_used > runner_config.cpu_time_limit)
	        runner_result.status = TIME_LIMIT_EXCEEDED;
	      else if (runner_config.real_time_limit != RESOURCE_UNLIMITED && runner_result.real_time_used > runner_config.real_time_limit)
	        runner_result.status = TIME_LIMIT_EXCEEDED;
	      else if (runner_config.memory_limit != RESOURCE_UNLIMITED && runner_result.memory_used > runner_config.memory_limit)
	        runner_result.status = MEMORY_LIMIT_EXCEEDED;
	      else
	        runner_result.status = RUNTIME_ERROR;
	      break;
	    }
	}else {
		runner_result.exit_code = WEXITSTATUS(status);
	    log_debug("child process exit_code %d", runner_result.exit_code);

	    if (runner_result.exit_code != 0)
	    {
	      runner_result.status = RUNTIME_ERROR;
	    }
	    else
	    {
	      if (runner_config.cpu_time_limit != RESOURCE_UNLIMITED && runner_result.cpu_time_used > runner_config.cpu_time_limit)
	        runner_result.status = TIME_LIMIT_EXCEEDED;
	      else if (runner_config.real_time_limit != RESOURCE_UNLIMITED && runner_result.real_time_used > runner_config.real_time_limit)
	        runner_result.status = TIME_LIMIT_EXCEEDED;
	      else if (runner_config.memory_limit != RESOURCE_UNLIMITED && runner_result.memory_used > runner_config.memory_limit)
	        runner_result.status = MEMORY_LIMIT_EXCEEDED;
	      else
	        runner_result.status = COMPLETED;
	    }
	}

}
static int do_write_result_to_fd() {
	if (result_pipes[1]) {
		char buf[1024];
		int n = format_result(buf);
		return write(result_pipes[1], buf, n);
	}
	return 0;
}
static int sandbox_proxy(void *_arg)
{
	close(result_pipes[0]);
	close(msg_pipes[0]);

	pid_t inside_pid = fork();

	if (inside_pid < 0) {
		INTERNAL_ERROR_EXIT("Cannot run process, fork failed");
	} else if (!inside_pid) {
		close(msg_pipes[1]);
		child_process();
		_exit(42);
	}

	if (write(msg_pipes[1], &inside_pid, sizeof(inside_pid)) != sizeof(inside_pid))
		INTERNAL_ERROR_EXIT("Proxy write to pipe failed");
	monitor(inside_pid);

	return do_write_result_to_fd();
}
static void find_box_pid(void)
{
	char namebuf[256];
  	snprintf(namebuf, sizeof(namebuf), "/proc/%d/task/%d/children", (int)proxy_pid, (int)proxy_pid);
  	FILE *f = fopen(namebuf, "r");
  	if (!f)
    	return;

  	int child;
  	if (fscanf(f, "%d", &child) != 1)
  	{
	    fclose(f);
	    return;
  	}
  	box_pid = child;
  	if (fscanf(f, "%d", &child) == 1)
    	INTERNAL_ERROR_EXIT("Error parsing %s: unexpected children found", &namebuf);

  	fclose(f);
}

static void box_keeper(void)
{
  close(result_pipes[1]);
  close(msg_pipes[1]);

  struct rusage rus;
  int stat;
  pid_t p;
  p = wait4(proxy_pid, &stat, 0, &rus);
  if (p < 0)
  {
    INTERNAL_ERROR_EXIT("wait4");
  }
  if (p != proxy_pid)
    INTERNAL_ERROR_EXIT("wait4: unknown pid %d exited!", p);
  proxy_pid = 0;

  char inside_result[1024];
  int n = read(result_pipes[0], inside_result, sizeof(inside_result) - 1);
  if (n > 0)
  {
    inside_result[n] = '\0';
    if (runner_config.save_file)
    {
      log_debug("save result into file %s", runner_config.save_file);
      write_file(runner_config.save_file, inside_result);
    }
    else
    {
      log_debug("print result_message");
      printf("%s\n", inside_result);
    }
    log_debug("result  %s", inside_result);
  }
}

void run_in_sandbox() {
	char *stack;
	char *stackTop;

	stack = mmap(NULL, STACK_SIZE, PROT_READ | PROT_WRITE,
				 MAP_PRIVATE | MAP_ANONYMOUS | MAP_STACK, -1, 0);
	if (stack == MAP_FAILED) {
		INTERNAL_ERROR_EXIT("Cannot run proxy, mmap err");
	}
	setup_pipe(result_pipes, 1);
	setup_pipe(msg_pipes, 0);

	stackTop = stack + STACK_SIZE;
	proxy_pid = clone(
      sandbox_proxy,
      stackTop,
      SIGCHLD | (runner_config.share_net ? 0 : CLONE_NEWNET) | CLONE_NEWNS | CLONE_NEWPID | CLONE_NEWIPC | CLONE_NEWUTS,
      0);
  	if (proxy_pid < 0)
    	INTERNAL_ERROR_EXIT("Cannot run proxy, clone failed");
  	if (!proxy_pid)
    	INTERNAL_ERROR_EXIT("Cannot run proxy, clone returned 0");
	pid_t box_pid_inside_ns;
	int n = read(msg_pipes[0], &box_pid_inside_ns, sizeof(box_pid_inside_ns));
	if (n != sizeof(box_pid_inside_ns))
    	INTERNAL_ERROR_EXIT("Proxy failed before it passed box_pid");
	find_box_pid();
  	log_debug("Started proxy_pid=%d sandbox_pid=%d inside_ns_sandbox_pid=%d", (int)proxy_pid, (int)box_pid, (int)box_pid_inside_ns);

  	box_keeper();
}














