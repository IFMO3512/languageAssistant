package com.fivehundredtwelve.langassist.dictionaries;

import com.fivehundredtwelve.langassist.Languages;
import com.fivehundredtwelve.langassist.Word;
import com.fivehundredtwelve.langassist.interfaces.DictionaryManager;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DictionaryManagerImplTest {
    private final static Word WORD = new Word("word", Languages.ENGLISH);

    private final static Word TRANSLATION = new Word("das Wort", Languages.GERMAN);

    private DictionaryManager dictionaryManager;

    @Before
    public void setUp() throws Exception {
        dictionaryManager = new DictionaryManagerImpl();
    }

    @Test
    public void testAddWord() throws Exception {
        dictionaryManager.addWord(WORD);

        assertTrue("The word should be properly added", dictionaryManager.getWords().contains(WORD));
    }

    @Test
    public void testTranslationAddition() throws Exception {
        dictionaryManager.addTranslation(WORD, TRANSLATION);

        assertEquals("Both words should be added to dictionaryManager", 2, dictionaryManager.getWords().size());
        assertTrue("Both words should be added to dictionaryManager", dictionaryManager.getWords().contains(WORD));
        assertTrue("Both words should be added to dictionaryManager",
                dictionaryManager.getWords().contains(TRANSLATION));

        assertEquals("Translation should be added to dictionaryManager and getTransactions() should contain this",
                1, dictionaryManager.getTranslations().size());
        assertTrue("Translation should be added to dictionaryManager and getTransactions() should contain this",
                dictionaryManager.getTranslations().get(WORD).contains(TRANSLATION));
        assertNull("Translation should be added to dictionaryManager and getTransactions() shouldn't contain " +
                "translation's translation ", dictionaryManager.getTranslations().get(TRANSLATION));

        assertEquals("Translation should be added to dictionaryManager and getTransaction() should contain it",
                TRANSLATION, dictionaryManager.getTranslation(WORD, Languages.GERMAN));
        assertNull("Translation should be added to dictionaryManager and getTransaction() shouldn't contain " +
                "translation to other language ", dictionaryManager.getTranslation(WORD, Languages.FRENCH));
        assertNull("Translation should be added to dictionaryManager and getTransaction() shouldn't contain " +
                "translation's translation ", dictionaryManager.getTranslation(TRANSLATION, Languages.ENGLISH));
    }
}