#ifndef CONSTANTS_HEADER
#define CONSTANTS_HEADER

#include <string.h>
#include <signal.h>

#include "log.h"
#include "utils.h"

// 自定义信号
// 要执行的程序没有找到
#define SIGNF __SIGRTMIN + 10

// error_code 的错误代码
// 要运行的命令没有找到
#define COMMAND_NOT_FOUND 1

// 判题中
#define PENDING -1
// 答案正确
#define COMPLETED 0
// 超时
#define TIME_LIMIT_EXCEEDED 1
// 超内存限制
#define MEMORY_LIMIT_EXCEEDED 2
// 用户的程序运行时发生错误
#define RUNTIME_ERROR 3
// 编译错误
#define COMPILE_ERROR 4
// 判题系统发生错误
#define SYSTEM_ERROR 5

// 资源相关
#define RESOURCE_UNLIMITED 0
#define CMD_MAX_LENGTH 20
#define CALLS_MAX 400
#define LIMITS_MAX_OUTPUT (128 * 1024 * 1024)
#define LIMITS_MAX_FD 1024
#define STACK_SIZE (10 * 1024 * 1024) /* 10M Stack size(bytes) for cloned child */

struct Result
{
  int status;
  int cpu_time_used;//毫秒
  int real_time_used;
  int memory_used;
  int signal_code;
  int exit_code;
  int error_code;
  long cpu_time_used_us;//微妙
  long real_time_used_us;
};

struct Config
{
  int cpu_time_limit;
  int real_time_limit;
  int memory_limit;
  int memory_check_only;
  int attach_stdin;
  int attach_stdout;
  int attach_stderr;
  int share_net;
  char *stdin_file;
  char *stdout_file;
  char *stderr_file;
  char *testdata_out;
  char *save_file;
  char *log_file;
  char **cmd;
};
//_errno=2找不到指定文件的错误
#define CHILD_ERROR_EXIT(message)                                                               \
  {                                                                                             \
    int _errno = errno;                                                                         \
    log_fatal("child error: %s, errno: %d, strerror: %s; ", message, _errno, strerror(_errno)); \
    CLOSE_FD(input_fd);                                                                         \
    CLOSE_FD(output_fd);                                                                        \
    CLOSE_FD(err_fd);                                                                           \
    CLOSE_FD(null_fd);                                                                          \
    if (_errno == 2)                                                                            \
      raise(SIGNF);                                                                             \
    else                                                                                        \
      raise(SIGUSR1);                                                                           \
  }
//程序内部发生错误
#define INTERNAL_ERROR_EXIT(message, arg...)                                       \
  {                                                                                \
    log_fatal("Interlnal Error: errno: %d, strerror: %s", errno, strerror(errno)); \
    log_fatal(message, ##arg);                                                     \
    exit(EXIT_FAILURE);                                                            \
  }

#endif
