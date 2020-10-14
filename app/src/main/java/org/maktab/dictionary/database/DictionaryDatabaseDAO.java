package org.maktab.dictionary.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.maktab.dictionary.model.DictionaryWord;

import java.util.List;

@Dao
public interface DictionaryDatabaseDAO {

    @Insert
    void insertWord(DictionaryWord word);

    @Update
    void updateWord(DictionaryWord word);

    @Delete
    void deleteWord(DictionaryWord word);

    @Query("SELECT * FROM dictionaryTable")
    List<DictionaryWord> getWords();

    @Query("SELECT * FROM dictionaryTable WHERE id=:inputId")
    DictionaryWord getWord(long inputId);
}
