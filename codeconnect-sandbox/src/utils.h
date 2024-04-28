#ifndef UTILS_HEADER
#define UTILS_HEADER

#include <sys/time.h>

#define CLOSE_FD(fd) \
  if (fd > 0)        \
  {                  \
    close(fd);       \
  }

#define CLOSE_FP(fp) \
  if (fp != NULL)    \
  {                  \
    fclose(fp);      \
  }

int str_equal(const char *, const char *);
int format_result(char *);
void log_config();
long tv_to_ms(const struct timeval *tv);
long tv_to_us(const struct timeval *tv);
int write_file(const char *, const char *);
void setup_pipe(int *fds, int nonblocking);
size_t join_str(char *out_string, size_t out_bufsz, const char *delim, char **chararr);
int kill_pid(pid_t pid);

#endif
