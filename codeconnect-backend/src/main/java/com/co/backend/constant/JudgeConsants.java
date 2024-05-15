package com.co.backend.constant;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-05-10 8:27
 */

public class JudgeConsants {
    public enum Judge {
        STATUS_NOT_SUBMITTED(-10, "Not Submitted", null),
        STATUS_SUBMITTED_UNKNOWN_RESULT(-5, "Submitted Unknown Result", null),
        STATUS_CANCELLED(-4, "Cancelled", "ca"),
        STATUS_PRESENTATION_ERROR(-3, "Presentation Error", "pe"),
        STATUS_WRONG_ANSWER(-1, "Wrong Answer", "wa"),
        STATUS_ACCEPTED(0, "Accepted", "ac"),
        STATUS_TIME_LIMIT_EXCEEDED(1, "Time Limit Exceeded", "tle"),
        STATUS_MEMORY_LIMIT_EXCEEDED(2, "Memory Limit Exceeded", "mle"),
        STATUS_RUNTIME_ERROR(3, "Runtime Error", "re"),
        STATUS_COMPILE_ERROR(4, "Compile Error", "ce"),
        STATUS_SYSTEM_ERROR(5, "System Error", "se"),
        STATUS_PENDING(6, "Pending", null),
        STATUS_COMPILING(7, "Compiling", null),
        STATUS_JUDGING(8, "Judging", null),
        STATUS_PARTIAL_ACCEPTED(9, "Partial Accepted", "pa"),
        STATUS_SUBMITTING(10, "Submitting", null),
        STATUS_SUBMITTED_FAILED(11, "Submitted Failed", null),
        STATUS_NULL(15, "No Status", null),
        JUDGE_SERVER_SUBMIT_PREFIX(-1002, "Judge SubmitId-ServerId:", null);
        private final Integer status;
        private final String name;

        private final String columnName;
        private Judge(Integer status, String name, String columnName) {
            this.status = status;
            this.name = name;
            this.columnName = columnName;
        }
        public Integer getStatus() {
            return status;
        }

        public String getName() {
            return name;
        }

        public String getColumnName() {
            return columnName;
        }

    }

}
