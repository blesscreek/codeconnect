package com.co.common.constants;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-05-16 21:39
 */

public class LanguageConstants {
    public enum SpecialRule{
        JAVA("Main", "Main.class");
        private final String name;
        private final String compiledName;
        SpecialRule(String name, String compiledName) {
            this.name = name;
            this.compiledName = compiledName;
        }

        public String getName() {
            return name;
        }
        public String getCompiledName() {
            return compiledName;
        }
    }
    public enum Language {
        C("C", ".c"),
        CPP("Cpp", ".cpp"),
        JAVA("Java", ".java"),
        GO("Go",".go"),
        PYTHON("Python",".py"),
        JAVASCRIPT("JavaScript",".js");

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
