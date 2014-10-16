package com.fivehundredtwelve.langassist;

import java.io.Serializable;

import com.google.common.base.Preconditions;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Class that represent the word entity.
 * This class is immutable.
 * <p>
 * Created by eliseev on 19/09/14.
 * 
 */
public class Word implements Serializable{
	
	private static final long serialVersionUID = -5075958923027437396L;
	private final String word;
    private final Languages language;

    /**
     * Creates a {@link Word} object with specified email.
     *
     * @param word the word itself 
     * @param language language, bounded to this word
     * @throws java.lang.NullPointerException if word is null
     */
    public Word(final String word, final Languages language) {
        Preconditions.checkNotNull(word);
        Preconditions.checkNotNull(language);

        this.language = language;
        this.word = word;
    }

    public Languages getLanguage() {
        return language;
    }

    public String getWord() {
        return word;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Word)) return false;

        Word word1 = (Word) o;

        if (!word.equals(word1.word)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return word.hashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("word", word)
                .toString();
    }
}
