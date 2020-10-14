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
    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, DictionaryDetail.class);
        return intent;
    }
    @Override
    public Fragment createFragment() {
        DictionaryDetailFragment dictionaryDetailFragment = DictionaryDetailFragment.newInstance();
        return dictionaryDetailFragment;
    }
}