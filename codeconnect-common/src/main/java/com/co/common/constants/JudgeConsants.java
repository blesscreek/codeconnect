package com.co.common.constants;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-05-10 8:27
 */

public class JudgeConsants {
    public enum JudgeColor {
        AC("rgb(82, 196, 26)"),
        WA("rgb(231, 76, 60)"),
        EXCEEDLIMIT("rgb(5, 34, 66)"),
        ;
        private String color;
        JudgeColor(String color) {
            this.color = color;
        }
        public String getColor() {
            return color;
        }
    }
    public enum EnvName {
        SANDBOXPATH("/codeconnect-sandbox"),
        OUTPUTPATH("tmp.out")
        ;
        private String name;
        EnvName(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }
    }
    public enum LimitNum {
        TIME_LIMIT(10 * 1000L),
        MEMORY_LIMIT(512 * 1024L)
        ;
        private Long num;
        LimitNum(Long num) {
            this.num = num;
        }
        public Long getNum() {
            return num;
        }
    }

    public enum Judge {
        STATUS_NOT_SUBMITTED(-10, "未提交","Not Submitted",null),
        STATUS_SUBMITTED_UNKNOWN_RESULT(-5, "提交结果未知","Submitted Unknown Result", null),
        STATUS_CANCELLED(-4, "取消提交","Cancelled", "ca"),
        STATUS_COMPILING(-2, "编译中","Compiling", null),
        STATUS_PENDING(-1, "等待评判中","Pending", null),
        STATUS_ACCEPTED(0, "通过","Accepted", "ac"),
        PATTERN_ERROR(1, "格式有误","Pattern Error", "pe"),
        STATUS_TIME_LIMIT_EXCEEDED(2, "时间超限","Time Limit Exceeded", "tle"),
        STATUS_MEMORY_LIMIT_EXCEEDED(3, "内存超限","Memory Limit Exceeded", "mle"),
        STATUS_WRONG_ANSWER(4, "答案错误","Wrong Answer", "wa"),
        STATUS_RUNTIME_ERROR(5, "运行时错误","Runtime Error", "re"),
        STATUS_COMPILE_ERROR(6, "编译错误","Compile Error", "ce"),
        STATUS_SYSTEM_ERROR(7, "系统错误","System Error", "se"),
        STATUS_JUDGING(8, "评判中","Judging", null),
        STATUS_PARTIAL_ACCEPTED(9, "部分正确","Partial Accepted", "pa"),
        STATUS_SUBMITTING(10, "提交中","Submitting", null),
        STATUS_SUBMITTED_FAILED(11, "提交失败","Submitted Failed", null),
        STATUS_NULL(15, "无状态","No Status", null),
        JUDGE_SERVER_SUBMIT_PREFIX(-1002, "某种特定错误","Judge SubmitId-ServerId:", null);
        private final Integer status;
        private final String name;

        private final String nameEng;

        private final String columnName;

        private Judge(Integer status, String name,String nameEng, String columnName) {
            this.status = status;
            this.name = name;
            this.nameEng = nameEng;
            this.columnName = columnName;
        }
        public Integer getStatus() {
            return status;
        }

        public String getName() {
            return name;
        }

        public String getNameEng(){return nameEng;}

        public String getColumnName() {
            return columnName;
        }

        public static String getColumnNameFromStatus(Integer status) {
            for (Judge judge : Judge.values()) {
                if (judge.getStatus().equals(status)) {
                    return judge.getColumnName();
                }
            }
            return null;
        }
        public static String getNameFromStatus(Integer status) {
            for (Judge judge : Judge.values()) {
                if (judge.getStatus().equals(status)) {
                    return judge.getName();
                }
            }
            return null;
        }
        public static String getNameEngFromStatus(Integer status) {
            for (Judge judge : Judge.values()) {
                if (judge.getStatus().equals(status)) {
                    return judge.getNameEng();
                }
            }
            return null;
        }

    }

}
