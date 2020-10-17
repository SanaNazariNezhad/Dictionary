package org.maktab.dictionary.controller.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.maktab.dictionary.R;
import org.maktab.dictionary.controller.fragment.DictionaryDetailFragment;
import org.maktab.dictionary.controller.fragment.DictionaryListFragment;

public class DictionaryDetail extends SingleFragmentActivity {
    public static final String EXTRA_WORD_ID = "Extra_WordID.org.maktab.dictionary.controller.activity";
    public static final String EXTRA_FROM = "Extra_FROM.org.maktab.dictionary.controller.activity";
    public static final String EXTRA_TO = "Extra_TO.org.maktab.dictionary.controller.activity";
    private static long mWordID;
    private static String mFrom,mTo;
    public static Intent newIntent(Context context,long wordId,String from,String to) {
        mWordID = wordId;
        mFrom = from;
        mTo = to;
        Intent intent = new Intent(context, DictionaryDetail.class);
        intent.putExtra(EXTRA_WORD_ID,wordId);
        intent.putExtra(EXTRA_FROM,mFrom);
        intent.putExtra(EXTRA_TO,mTo);
        return intent;
    }
    @Override
    public Fragment createFragment() {
        DictionaryDetailFragment dictionaryDetailFragment = DictionaryDetailFragment.newInstance(mWordID,mFrom,mTo);
        return dictionaryDetailFragment;
    }
}