package com.fivehundredtwelve.langassist.dictionaries;

import com.fivehundredtwelve.langassist.Word;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class DictionaryManagerImplTest {
    private final static Word WORD = new Word("word");

    private DictionaryManagerImpl dictionaryManager;

    @Before
    public void setUp() throws Exception {
        dictionaryManager = new DictionaryManagerImpl();
    }

    @Test
    public void testAddWord() throws Exception {

        dictionaryManager.addWord(WORD);

        assertTrue("The word should be properly added", dictionaryManager.getWords().contains(WORD));
    }
}