#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/time.h>
#include <limits.h>
#include <fcntl.h>
#include <errno.h>
#include <signal.h>

extern struct Config runner_config;
extern struct Result runner_result;

#include "constants.h"
#include "log.h"
#include "utils.h"

int str_equal(const char *s, const char *s2)
{
  while (*s && *s2)
  {
    if (*s++ != *s2++)
    {
      return 0;
    }
  }

  return 1;
}

int format_result(char *message)
{
  //sprintf()发送格式化输出到message
  return sprintf(message, "{\n"
                          "  \"status\": %d,\n"
                          "  \"cpu_time_used\": %d,\n"
                          "  \"cpu_time_used_us\": %ld,\n"
                          "  \"real_time_used\": %d,\n"
                          "  \"real_time_used_us\": %ld,\n"
                          "  \"memory_used\": %d,\n"
                          "  \"error_code\": %d,\n"
                          "  \"signal_code\": %d,\n"
                          "  \"exit_code\": %d\n"
                          "}",
                 runner_result.status,
                 runner_result.cpu_time_used,
                 runner_result.cpu_time_used_us,
                 runner_result.real_time_used,
                 runner_result.real_time_used_us,
                 runner_result.memory_used,
                 runner_result.error_code,
                 runner_result.signal_code,
                 runner_result.exit_code);
}

void log_config()
{
  char buf[256] = {'\0'};
  join_str(buf, sizeof(buf), " ", runner_config.cmd);
  log_debug("config: cmd %s", buf);
  log_debug("config: memory_check_only %d", runner_config.memory_check_only);
  log_debug("config: cpu_time_limit %d ms", runner_config.cpu_time_limit);
  log_debug("config: real_time_limit %d ms", runner_config.real_time_limit);
  log_debug("config: memory_limit %d kb", runner_config.memory_limit);
  log_debug("config: attach: STDIN %d | STDOUT %d | STDERR %d", runner_config.attach_stdin, runner_config.attach_stdout, runner_config.attach_stderr);
  log_debug("config: stdin_file %s", runner_config.stdin_file);
  log_debug("config: testdata_out %s", runner_config.testdata_out);
  log_debug("config: savefile %s", runner_config.save_file);
  log_debug("config: stdout_file %s", runner_config.stdout_file);
  log_debug("config: stderr_file %s", runner_config.stderr_file);
  log_debug("config: log_file %s", runner_config.log_file);
}
//将时间转换成毫秒数，并对微秒部分进行四舍五入
long tv_to_ms(const struct timeval *tv)
{
  return (tv->tv_sec * 1000) + ((tv->tv_usec + 500) / 1000);
}
//时间转微秒
long tv_to_us(const struct timeval *tv)
{
  return (tv->tv_sec * 1000 * 1000) + tv->tv_usec;
}

int write_file(const char *filename, const char *content)
{
  FILE *fptr = fopen(filename, "w");
  if (fptr == NULL)
  {
    log_error("open %s error", filename);
    return 1;
  }
  fprintf(fptr, "%s", content);
  fclose(fptr);
  return 0;
}

void setup_pipe(int *fds, int nonblocking)
{
  //操作系统在内核分配一个管道缓冲区，创建两个文件描述符，指向内核中的管道对象
  //在文件描述符表中找到文件描述符a对应的管道对象，并将东西写入内核中的管道缓冲区
  if (pipe(fds) < 0)
  {
    INTERNAL_ERROR_EXIT("pipe");
  }
  for (int i = 0; i < 2; i++)
  {
    //设置文件描述符，执行exec函数使会自动关闭该文件描述符
    //将文件描述符的状态标志设置为非阻塞模式，即在读写该文件描述符时，若无数据可读或无空间可写，操作将立即返回，而不会被阻塞。
    if (fcntl(fds[i], F_SETFD, fcntl(fds[i], F_GETFD) | FD_CLOEXEC) < 0 ||
        (nonblocking && fcntl(fds[i], F_SETFL, fcntl(fds[i], F_GETFL) | O_NONBLOCK) < 0))
    {
      INTERNAL_ERROR_EXIT("fcntl on pipe");
    }
  }
}

/**
 * https://stackoverflow.com/questions/4681325/join-or-implode-in-c
 * Thanks to bdonlan.
 */
 //字符串追加
static char *util_cat(char *dest, char *end, const char *str)
{
  while (dest < end && *str)
    *dest++ = *str++;
  return dest;
}
//out_string输出字符串缓冲区的指针
//out_bufsz输出字符串缓冲区的大小
//delim指向分隔符字符串的指针
//chararr指向字符串数组的指针数组
size_t join_str(char *out_string, size_t out_bufsz, const char *delim, char **chararr)
{
  char *ptr = out_string;
  //指向字符串末尾的指针
  char *strend = out_string + out_bufsz;
  while (ptr < strend && *chararr)
  {
    //将*chararr内容追加到ptr指针指向的位置
    ptr = util_cat(ptr, strend, *chararr);
    chararr++;
    if (*chararr)
    {
    //将分隔符追加到输出字符串缓冲区
      ptr = util_cat(ptr, strend, delim);
    }
  }
  //连接后字符串的长度，并返回长度值
  return ptr - out_string;
}

int kill_pid(pid_t pid)
{
  return kill(pid, SIGKILL);
}
