package org.maktab.dictionary.controller.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.maktab.dictionary.R;
import org.maktab.dictionary.controller.fragment.DictionaryListFragment;

public class DictionaryList extends SingleFragmentActivity {

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, DictionaryList.class);
        return intent;
    }
    @Override
    public Fragment createFragment() {
        DictionaryListFragment dictionaryListFragment = DictionaryListFragment.newInstance();
        return dictionaryListFragment;
    }

}