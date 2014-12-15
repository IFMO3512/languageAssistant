package com.fivehundredtwelve.langassist.controller.rest;

import com.fivehundredtwelve.langassist.Word;

import java.util.Collection;

/**
 * @author eliseev
 */ // TODO write anything!!! 
public class Quest {
    private final String word;

    private final Collection<Word> options;

    private final int answer;

    public Quest(final String word, final Collection<Word> options, final int answer) {
        this.word = word;
        this.options = options;
        this.answer = answer;
    }

    public String getWord() {
        return word;
    }

    public Collection<Word> getOptions() {
        return options;
    }

    public int getAnswer() {
        return answer;
    }
}
