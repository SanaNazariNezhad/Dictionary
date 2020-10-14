package org.maktab.dictionary.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import org.maktab.dictionary.model.DictionaryWord;

@Database(entities = {DictionaryWord.class},version = 1)
public abstract class DictionaryDatabase extends RoomDatabase {

    public abstract DictionaryDatabaseDAO getDictionaryDatabaseDAO();
}
