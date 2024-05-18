package com.co.common.constants;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-05-16 21:39
 */

public class LanguageConstants {
    public enum Language {
        C("C", ".c"),
        CPP("C++", ".cpp"),
        JAVA("Java", ".java");

        private final String language;
        private final String extension;

        Language(String language, String extension) {
            this.language = language;
            this.extension = extension;
        }
        public String getLanguage() {
            return language;
        }
        public String getExtension() {
            return extension;
        }
        public static String getExtensionFromLanguage(String language) {
            for (Language lang : Language.values()) {
                if (lang.getLanguage().equals(language)) {
                    return lang.getExtension();
                }
            }
            return null;
        }

    }

}
