package com.fivehundredtwelve.langassist;

import javax.annotation.Nullable;

/**
 * @author eliseev
 */
public enum Language {

    ENGLISH("English"),
    RUSSIAN("Russian", "Русский"),
    FRENCH("French", "Français"),
    GERMAN("German", "Deutsch"),
    ITALIAN("Italian", "Italiano"),
    SPANISH("Spanish", "Español");
    private final String languageEnglishName;
    private final String languageName;

    Language(String languageName) {
        this.languageEnglishName = languageName;
        this.languageName = languageName;
    }

    Language(String languageEnglishName, String languageName) {
        this.languageName = languageName;
        this.languageEnglishName = languageEnglishName;
    }

    @Nullable
    public static Language getLanguage(String languageName) {
        for (Language language : Language.values()) {
            if (language.languageName.equalsIgnoreCase(languageName) ||
                    language.languageEnglishName.equalsIgnoreCase(languageName))
                return language;
        }

        return null;
    }

    public String getLanguageEnglishName() {
        return languageEnglishName;
    }

    public String getLanguageName() {
        return languageName;
    }
}
