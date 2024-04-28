#include "constants.h"

struct Result runner_result = {
    .status = PENDING,
    .cpu_time_used = 0,
    .real_time_used = 0,
    .memory_used = 0,
    .signal_code = 0,
    .exit_code = 0,
    .error_code = 0,
    .cpu_time_used_us = 0,
    .real_time_used_us = 0,
};

struct Config runner_config = {
    .cpu_time_limit = 0,
    .real_time_limit = 0,
    .memory_limit = 0,
    .memory_check_only = 0,
    .attach_stdin = 0,
    .attach_stdout = 0,
    .attach_stderr = 0,
    .share_net = 0,
    .stdin_file = '\0',
    .stdout_file = '\0',
    .stderr_file = '\0',
    .testdata_out = '\0',
    .save_file = '\0',
    .log_file = "runner.log",
};
