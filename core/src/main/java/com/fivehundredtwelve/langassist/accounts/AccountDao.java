package com.fivehundredtwelve.langassist.accounts;

import com.fivehundredtwelve.langassist.User;
import com.fivehundredtwelve.langassist.Word;

import java.util.List;

/**
 * @author eliseev
 */ // TODO write anything!!! 
public interface AccountDao {
    void addUser(User user);

    User getUser(String email);

    void addWordToUser(User user, Word word);

    List<Word> getWords(User user);

    void removeWord(User user, Word word);
}
