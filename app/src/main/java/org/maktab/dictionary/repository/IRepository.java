package org.maktab.dictionary.repository;

import org.maktab.dictionary.model.DictionaryWord;

import java.util.List;

public interface IRepository {

    void insertWord(DictionaryWord word);
    void updateWord(DictionaryWord word);
    void deleteWord(DictionaryWord word);
    List<DictionaryWord> getWords();
    DictionaryWord getWord(long inputId);
}
