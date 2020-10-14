package org.maktab.dictionary.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "dictionaryTable")
public class DictionaryWord {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long primaryId;

    @ColumnInfo(name = "Arabic")
    private String mArabic;

    @ColumnInfo(name = "English")
    private String mEnglish;

    @ColumnInfo(name = "French")
    private String mFrench;

    @ColumnInfo(name = "Persian")
    private String mPersian;

    public long getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(long primaryId) {
        this.primaryId = primaryId;
    }

    public String getArabic() {
        return mArabic;
    }

    public void setArabic(String arabic) {
        mArabic = arabic;
    }

    public String getEnglish() {
        return mEnglish;
    }

    public void setEnglish(String english) {
        mEnglish = english;
    }

    public String getFrench() {
        return mFrench;
    }

    public void setFrench(String french) {
        mFrench = french;
    }

    public String getPersian() {
        return mPersian;
    }

    public void setPersian(String persian) {
        mPersian = persian;
    }

    public DictionaryWord(String arabic, String english, String french, String persian) {
        mArabic = arabic;
        mEnglish = english;
        mFrench = french;
        mPersian = persian;
    }
}
