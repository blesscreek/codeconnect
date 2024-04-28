#define _GNU_SOURCE

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

#include "argv.h"
#include "constants.h"
#include "log.h"
#include "utils.h"
#include "sandbox.h"

extern struct Config runner_config;
extern struct Result runner_result;

FILE *log_fp = NULL;

void clean()
{
  CLOSE_FP(log_fp);
}

int main(int argc, char *argv[])
{
  log_set_quiet(true);
  parse_argv(argc, argv);

  if (runner_config.log_file)
  {
    log_fp = fopen(runner_config.log_file, "a");
    if (log_fp != NULL)
    {
      log_add_fp(log_fp, LOG_DEBUG);
      log_config();
    }
  }
  if (geteuid())
  {
    fprintf(stderr, "Must be started as root\n");
    return 1;
  }

  if (getegid() && setegid(0) < 0)
  {
    fprintf(stderr, "Cannot switch to root group\n");
    return 1;
  }

  run_in_sandbox();
  //当程序正常终止，调用clean函数
  atexit(clean);
  return 0;
}
