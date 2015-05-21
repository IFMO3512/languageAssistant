package com.fivehundredtwelve.langassist.dictionaries;

import com.fivehundredtwelve.langassist.Language;
import com.fivehundredtwelve.langassist.Word;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

@Ignore
public class DictionaryManagerImplTest {
    private final static Word WORD = new Word("word", Language.ENGLISH);

    private final static Word TRANSLATION = new Word("das Wort", Language.GERMAN);

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
                TRANSLATION, dictionaryManager.getTranslation(WORD, Language.GERMAN));
        assertNull("Translation should be added to dictionaryManager and getTransaction() shouldn't contain " +
                "translation to other language ", dictionaryManager.getTranslation(WORD, Language.FRENCH));
        assertNull("Translation should be added to dictionaryManager and getTransaction() shouldn't contain " +
                "translation's translation ", dictionaryManager.getTranslation(TRANSLATION, Language.ENGLISH));

        dictionaryManager.removeWord(TRANSLATION);

        assertEquals("List of translation for word should be empty after deleting the translation", 0,
                dictionaryManager.getTranslations(WORD).size());
        assertEquals("List of translation for word should be empty after deleting the translation", 1,
                dictionaryManager.getWords().size());
        assertFalse("After the remove of word, words list should contain only translation",
                dictionaryManager.getWords().contains(TRANSLATION));

        dictionaryManager.removeWord(WORD);
        assertEquals("After the remove of word, translation list should be empty",
                0, dictionaryManager.getTranslations().size());
        assertEquals("After the remove of word and translation, words list should be empty",
                0, dictionaryManager.getWords().size());
        assertFalse("After the remove of word, words list should be empty",
                dictionaryManager.getWords().contains(WORD));
    }
}