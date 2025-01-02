/**
 * Loco program runner core
 * Copyright (C) 2011  Lodevil(Du Jiong)
 * Copyright (C) 2021  lengthmin(Artin)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

#include <sys/mman.h>
#include <sys/types.h>
#include <unistd.h>
#include <stdio.h>
#include <errno.h>
#include <string.h>
#include <fcntl.h>

#include "constants.h"
#include "diff.h"
#include "log.h"
#include "utils.h"

extern struct Config runner_config;
extern struct Result runner_result;

#define RETURN(rst)             \
  {                             \
    runner_result.status = rst; \
    return 0;                   \
  }

int check_diff(int rightout_fd, int userout_fd)
{
  char *userout, *rightout;
  const char *cuser, *cright, *end_user, *end_right;

  off_t userout_len, rightout_len;
  //获取文件长度
  rightout_len = lseek(rightout_fd, 0, SEEK_END);
  userout_len = lseek(userout_fd, 0, SEEK_END);

  if (userout_len == -1)
  {
    log_error("lseek userout_len failure: %s\n", strerror(errno));
    RETURN(SYSTEM_ERROR);
  }

  if (rightout_len == -1)
  {
    log_error("lseek userout_len failure: %s\n", strerror(errno));
    RETURN(SYSTEM_ERROR);
  }
  //回到文件开头
  lseek(userout_fd, 0, SEEK_SET);
  lseek(rightout_fd, 0, SEEK_SET);

  // 说明两个文件有一个长度为 0
  if ((userout_len && rightout_len) == 0)
  {
    // 如果有一个不为 0，则答案错误
    if (userout_len || rightout_len)
      RETURN(WRONG_ANSWER)
    else
      RETURN(ACCEPTED)
  }
  //内存映射文件
  if ((userout = (char *)mmap(NULL, userout_len, PROT_READ,
                              MAP_PRIVATE, userout_fd, 0)) == MAP_FAILED)
  {
    //释放内存映射文件
    munmap(userout, userout_len);
    log_error("mmap userout filure\n");
    RETURN(SYSTEM_ERROR);
  }

  if ((rightout = (char *)mmap(NULL, rightout_len, PROT_READ,
                               MAP_PRIVATE, rightout_fd, 0)) == MAP_FAILED)
  {
    munmap(rightout, rightout_len);
    log_error("mmap right filure\n");
    RETURN(SYSTEM_ERROR);
  }

  if ((userout_len == rightout_len) && str_equal(userout, rightout))
  {
    munmap(userout, userout_len);
    munmap(rightout, rightout_len);
    RETURN(ACCEPTED);
  }

  cuser = userout;
  cright = rightout;
  end_user = userout + userout_len;
  end_right = rightout + rightout_len;
  while ((cuser < end_user) && (cright < end_right))
  {
    // 逃逸掉中间输出结果的行末回车换行输出，可以只判断最后一行的
    while ((cuser < end_user) && (*cuser == ' ' || *cuser == '\n' || *cuser == '\r' || *cuser == '\t'))
      cuser++;
    while ((cright < end_right) && (*cright == ' ' || *cright == '\n' || *cright == '\r' || *cright == '\t'))
      cright++;
    if (cuser == end_user || cright == end_right)
      break;
    if (*cuser != *cright)
      break;
    cuser++;
    cright++;
  }
  while ((cuser < end_user) && (*cuser == ' ' || *cuser == '\n' || *cuser == '\r' || *cuser == '\t'))
    cuser++;
  while ((cright < end_right) && (*cright == ' ' || *cright == '\n' || *cright == '\r' || *cright == '\t'))
    cright++;
  if (cuser == end_user && cright == end_right)
  {
    munmap(userout, userout_len);
    munmap(rightout, rightout_len);
    RETURN(PRESENTATION_ERROR);
  }

  munmap(userout, userout_len);
  munmap(rightout, rightout_len);
  RETURN(WRONG_ANSWER);
}

void diff()
{
  if (runner_config.testdata_out && runner_config.stdout_file)
  {
    //0_RDONLY只读
    //0644：0表示本地文件，644是文件权限，读（4）写（2）执行（1）
    //6拥有者：读写   4所属组：读    4：其他用户：读
    //返回值为文件描述符
    int right_fd = open(runner_config.testdata_out, O_RDONLY, 0644);
    int userout_fd = open(runner_config.stdout_file, O_RDONLY, 0644);
    check_diff(right_fd, userout_fd);
    CLOSE_FD(right_fd);
    CLOSE_FD(userout_fd);
  }
  else
    log_info("skip diff, testdata_out or stdout_file not set.");
}
