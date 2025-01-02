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
#include "diff.h"

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
	// /dev/null是特殊的文件，写入的数据会被丢弃，读取是返回EOF，重定向不需要的输入输出
	int null_fd = open("/dev/null", O_RDWR);

	if (runner_config.cpu_time_limit != RESOURCE_UNLIMITED) {
		SET_LIMIT(RLIMIT_CPU, (runner_config.cpu_time_limit + 1000)/ 1000);
	}
	if (runner_config.memory_limit != RESOURCE_UNLIMITED) {
		if (runner_config.memory_check_only == 0) {
			SET_LIMIT(RLIMIT_AS, runner_config.memory_limit * 1024 * 2);
		}
	}

    //进程可打开的最大文件描述符
	SET_LIMIT(RLIMIT_NOFILE, LIMITS_MAX_FD);
	SET_LIMIT(RLIMIT_FSIZE, LIMITS_MAX_OUTPUT);

    //标准输入重定向
	if (runner_config.stdin_file) {
		input_fd = open(runner_config.stdin_file, O_RDONLY | O_CREAT, 0700);
		if (input_fd != -1) {
			log_debug("open stdin_file");
			//标准输入从指定文件重定向
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
  	//执行（命令，命令及其参数数组）
  	execvp(runner_config.cmd[0], runner_config.cmd);
  	CHILD_ERROR_EXIT("exec cmd error");
}
void *timeout_killer(void *killer_para) {
  pid_t pid = ((struct killer_parameter *)killer_para)->pid;
  int timeout = ((struct killer_parameter *)killer_para)->timeout;
  //将当前线程设置为分离状态，分离线程结束时自动释放资源
  if (pthread_detach(pthread_self()) != 0) {
		kill_pid(pid);
		return NULL;
	}
	//休眠等待超时时间，成功返回0，说明睡到了超时时间
	if (sleep((unsigned int)((timeout + 500) / 1000)) == 0) {
	    //终止子进程
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
	//获取当前真实事件，表示对子进程的监控时间
	gettimeofday(&start_time, NULL);
	pthread_t tid = 0;
	//时间限制如果不是无限，则启动一个线程，在指定的时间内杀死子进程
	if (runner_config.real_time_limit != RESOURCE_UNLIMITED) {
		struct killer_parameter para =
            {
                .timeout = runner_config.real_time_limit,
                .pid = child_pid,
            };
        //创建新线程，执行timeout_killer函数，将上方para结构体作为参数传入。创建线程成功则返回0
		if (pthread_create(&tid, NULL, timeout_killer, (void *)(&para)) != 0) {
		    //线程创建失败，杀死子执行代码运行的子线程
			kill_pid(child_pid);
			INTERNAL_ERROR_EXIT("pthread(timeout_killer) create error");
		}
	}
	int status;
	struct rusage ru;
	//wait4等待子进程结束，该函数可收集子进程的资源使用信息，存到ru。
	//子进程的退出状态保存在status
	if (wait4(child_pid, &status, 0, &ru) == -1) {
		INTERNAL_ERROR_EXIT("wait4 error");
	}
	//捕捉结束时间
	gettimeofday(&end_time, NULL);
	//记录wait4收集的资源使用情况
	log_rusage(&ru);
	//记录运行子进程的运行状态
	//计算子进程消耗的总真实时间（墙上时间）
	const struct timeval real_time_tv = {
		(end_time.tv_sec - start_time.tv_sec),
      	(end_time.tv_usec - start_time.tv_usec),
	};
  	runner_result.real_time_used = tv_to_ms(&real_time_tv);
  	runner_result.real_time_used_us = tv_to_us(&real_time_tv);
  	//计算子进程的总CPU时间，即用户态和内核态的时间总和
  	const struct timeval cpu_time_tv = {
      ((&ru.ru_utime)->tv_sec + (&ru.ru_stime)->tv_sec),
      ((&ru.ru_utime)->tv_usec + (&ru.ru_stime)->tv_usec),
  	};
	runner_result.cpu_time_used = tv_to_ms(&cpu_time_tv);
  	runner_result.cpu_time_used_us = tv_to_us(&cpu_time_tv);
  	//记录子进程最大内存量
	runner_result.memory_used = ru.ru_maxrss;
	//子线程执行命令结束了，不再需要tid超时监控线程，取消该线程
  	if (runner_config.real_time_limit != RESOURCE_UNLIMITED)
  	{
  	    //取消子进程
    	if (pthread_cancel(tid) != 0)
    	{
      		log_debug("cancel fail");
    	};
  	}
  	//如果子进程是因为信号异常退出，使用WIFSIGNALED检测并记录导致退出的信号代码
	if (WIFSIGNALED(status)) {
	    //WTERMSIG获取导致子进程退出的信号编号并赋值给signal_code
		runner_result.signal_code = WTERMSIG(status);
    	log_debug("child process exit abnormal, signal: %d, strsignal: %s", runner_result.signal_code, strsignal(runner_result.signal_code));
   		switch (runner_result.signal_code)
	    {
	    //用户自定义信号，系统错误
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
	      //当前进程使用内存超内存限制，设置内存超限
	      if (runner_result.memory_used > runner_config.memory_limit)
	        runner_result.status = MEMORY_LIMIT_EXCEEDED;
	      else
	        //否则，运行时错误
	        runner_result.status = RUNTIME_ERROR;
	      break;
	    case SIGALRM://定时器信号，程序设定时间到达发出
	    case SIGXCPU://CPU时间超限信号，CPU使用时间超过设定上限
	      runner_result.status = TIME_LIMIT_EXCEEDED;
	      break;
	    case SIGKILL://强制终止进程信号，通常由系统或其他程序发出
	    default:
	      //CPU时间超限
	      if (runner_config.cpu_time_limit != RESOURCE_UNLIMITED && runner_result.cpu_time_used > runner_config.cpu_time_limit)
	        runner_result.status = TIME_LIMIT_EXCEEDED;
	      //真实事件超限
	      else if (runner_config.real_time_limit != RESOURCE_UNLIMITED && runner_result.real_time_used > runner_config.real_time_limit)
	        runner_result.status = TIME_LIMIT_EXCEEDED;
	      //内存限制
	      else if (runner_config.memory_limit != RESOURCE_UNLIMITED && runner_result.memory_used > runner_config.memory_limit)
	        runner_result.status = MEMORY_LIMIT_EXCEEDED;
	      //运行时异常
	      else
	        runner_result.status = RUNTIME_ERROR;
	      break;
	    }
	}else {
	    //进程正常退出，WEXITSTATUS获取退出状态码
		runner_result.exit_code = WEXITSTATUS(status);
	    log_debug("child process exit_code %d", runner_result.exit_code);
	    if (runner_result.exit_code != 0)
	    {
	      //状态码非0,说明运行时发生异常
	      runner_result.status = RUNTIME_ERROR;
	    }
	    else
	    {
	      //为0，检查是否超过设置的CPU时间、真实时间、内存限制
          if (runner_config.cpu_time_limit != RESOURCE_UNLIMITED && runner_result.cpu_time_used > runner_config.cpu_time_limit)
	        {
	        runner_result.status = TIME_LIMIT_EXCEEDED;
	        }
	      else if (runner_config.real_time_limit != RESOURCE_UNLIMITED && runner_result.real_time_used > runner_config.real_time_limit)
	        {
	        runner_result.status = TIME_LIMIT_EXCEEDED;
	        }

	      else if (runner_config.memory_limit != RESOURCE_UNLIMITED && runner_result.memory_used > runner_config.memory_limit)
	        {
	        runner_result.status = MEMORY_LIMIT_EXCEEDED;
	        }
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
    //沙盒进程启动后关闭管道读取段
	close(result_pipes[0]);
	close(msg_pipes[0]);

    //创建子进程
    //fork调用后，产生父子进程，两个进程分别执行相同代码，父进程中fork返回子进程的PID，子进程中，返回0
	pid_t inside_pid = fork();

	if (inside_pid < 0) {
		INTERNAL_ERROR_EXIT("Cannot run process, fork failed");
	} else if (!inside_pid) {
	    //子进程中
		close(msg_pipes[1]);
		child_process();
		//子进程执行child_process，执行结束时通过_exit（42）退出
		_exit(42);
	}

    //将inside_pid写入管道，方便父进程或其他进程获取子进程PID
	if (write(msg_pipes[1], &inside_pid, sizeof(inside_pid)) != sizeof(inside_pid))
		INTERNAL_ERROR_EXIT("Proxy write to pipe failed");
	monitor(inside_pid);
	// 子程序运行失败的话，直接输出结果。不需要进行后面的 diff 了
      if (runner_result.exit_code || runner_result.signal_code)
      {
        return do_write_result_to_fd();
      }
    //运行正常，则对输出进行比较
      if (runner_result.status <= ACCEPTED)
      {
        diff();
      }
      //结果写入文件描述符
      return do_write_result_to_fd();
}
//获取proxy_pid进程的一个子进程PID，并存储到box_pid变量
static void find_box_pid(void)
{
	char namebuf[256];
	//找到proxy_id并格式化字符串
  	snprintf(namebuf, sizeof(namebuf), "/proc/%d/task/%d/children", (int)proxy_pid, (int)proxy_pid);
  	FILE *f = fopen(namebuf, "r");
  	if (!f)
    	return;

  	int child;
    //读取到第一个子进程存入全局变量box_pid总
  	if (fscanf(f, "%d", &child) != 1)
  	{
	    fclose(f);
	    return;
  	}
  	box_pid = child;
  	//接着读取，如果读到第二个，表示存在多个子进程是错误的
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
  //等待代理进程结束
  p = wait4(proxy_pid, &stat, 0, &rus);
  if (p < 0)
  {
    INTERNAL_ERROR_EXIT("wait4");
  }
  if (p != proxy_pid)
    INTERNAL_ERROR_EXIT("wait4: unknown pid %d exited!", p);
  proxy_pid = 0;

  char inside_result[1024];
  //读取结果
  int n = read(result_pipes[0], inside_result, sizeof(inside_result) - 1);
  if (n > 0)
  {
    inside_result[n] = '\0';
    //设置了保存文件路径则写入，否则打印结果到标准输出
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
	char *stack;//文件起始地址的指针
	char *stackTop;

    // 希望文件映射到此指针指向的位置。 NULL 允许内核自己选择合适的地址
    //内存映像文件的安全属性（映像内存可读，可写）。
    //内存映像标志（对内存映像文件的改动不映像其他进程或文件。创建匿名映射，不与任何文件关联。映射作为线程的栈使用）
    //文件描述符（-1没有与之关联的文件）。映射的数据内容距离文件头的偏移量
    //返回指向内存映像文件起始地址的指针
    //为线程分配一个新的栈的内存区域，允许线程在其上操作
	stack = mmap(NULL, STACK_SIZE, PROT_READ | PROT_WRITE,
				 MAP_PRIVATE | MAP_ANONYMOUS | MAP_STACK, -1, 0);
	if (stack == MAP_FAILED) {
		INTERNAL_ERROR_EXIT("Cannot run proxy, mmap err");
	}

	setup_pipe(result_pipes, 1);
	setup_pipe(msg_pipes, 0);

	stackTop = stack + STACK_SIZE;
	//创建沙盒进程
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
	int n = read(msg_pipes[0], &box_pid_inside_ns, sizeof(box_pid_ins.de_ns));
	if (n != sizeof(box_pid_inside_ns))
    	INTERNAL_ERROR_EXIT("Proxy failed before it passed box_pid");
	find_box_pid();
	//proxy_pid是clone的进程
	//sandbox_pid是sandbox_proxy中直接创建的子进程,原始进程的上下文
	//box_pid_inside_ns是该子进程在命名空间内部的进程
  	log_debug("Started proxy_pid=%d sandbox_pid=%d inside_ns_sandbox_pid=%d", (int)proxy_pid, (int)box_pid, (int)box_pid_inside_ns);

  	box_keeper();
}