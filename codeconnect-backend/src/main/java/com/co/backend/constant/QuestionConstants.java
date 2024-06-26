package com.co.backend.constant;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-04-08 9:12
 */

public class QuestionConstants {
    public enum QuestionDifficulty {
        简单(0),
        中等(1),
        困难(2);
        private final Integer difficulty;

        QuestionDifficulty(Integer difficulty) {
            this.difficulty = difficulty;
        }
        public Integer getDifficulty() {
            return difficulty;
        }
        public static QuestionDifficulty getQuestionDifficulty(Integer difficulty) {
            for (QuestionDifficulty questionDifficulty : QuestionDifficulty.values()) {
                if (questionDifficulty.getDifficulty().equals(difficulty)) {
                    return questionDifficulty;
                }
            }
            return null;
        }
        public static Integer getQuestionDifficultyByString(String str) {
            for (QuestionDifficulty questionDifficulty : QuestionDifficulty.values()) {
                if (questionDifficulty.name().equals(str)) {
                    return questionDifficulty.getDifficulty();
                }
            }
            return null;
        }
    }
    public enum QuestionType {
        OI(0),
        ACM(1);
        private final Integer type;

        QuestionType(Integer type) {
            this.type = type;
        }

        public Integer getType() {
            return type;
        }

        public static QuestionType getQuestionType(Integer type) {
            for (QuestionType questionType : QuestionType.values()) {
                if (questionType.getType().equals(type)) {
                    return questionType;
                }
            }
            return null;
        }
    }
    public enum QuestionAuth {
        PUBLIC(0),
        PRIVATE(1);

        private final Integer auth;

        QuestionAuth(Integer auth) {
            this.auth = auth;
        }

        public Integer getAuth() {
            return auth;
        }

        public static QuestionAuth getquestionAuth(Integer auth) {
            for (QuestionAuth questionAuth : QuestionAuth.values()) {
                if (questionAuth.getAuth().equals(auth)) {
                    return questionAuth;
                }
            }
            return null;
        }
    }
    public enum JudgeMode {
        DEFAULT("default"),
        SPJ("spj");

        private final String mode;

        JudgeMode(String mode) {
                    this.mode = mode;
                }
        public String getMode() {
                    return mode;
                }
        public static JudgeMode getJudgeMode(String mode) {
            for (JudgeMode judgeMode : JudgeMode.values()) {
                if (judgeMode.getMode().equals(mode)) {
                    return judgeMode;
                }
            }
            return null;
        }
    }

}
