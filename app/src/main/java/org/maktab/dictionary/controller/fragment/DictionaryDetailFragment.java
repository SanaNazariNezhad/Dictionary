package org.maktab.dictionary.controller.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.maktab.dictionary.R;
import org.maktab.dictionary.model.DictionaryWord;
import org.maktab.dictionary.repository.DictionaryDBRepository;
import org.maktab.dictionary.repository.IRepository;

public class DictionaryDetailFragment extends Fragment {

    public static final String KEY_VALUE_WORD_ID = "key_value_WordId";
    public static final String KEY_VALUE_FROM = "key_value_FROM";
    public static final String KEY_VALUE_TO = "key_value_TO";
    private long mWordID;
    private String mFrom, mTo;
    private DictionaryWord mDictionaryWord;
    private IRepository mIRepository;
    private TextView mTextViewTitle, mTextViewMeaning;

    public DictionaryDetailFragment() {
        // Required empty public constructor
    }

    public static DictionaryDetailFragment newInstance(long wordId, String from, String to) {
        DictionaryDetailFragment fragment = new DictionaryDetailFragment();
        Bundle args = new Bundle();
        args.putLong(KEY_VALUE_WORD_ID, wordId);
        args.putString(KEY_VALUE_FROM, from);
        args.putString(KEY_VALUE_TO, to);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWordID = getArguments().getLong(KEY_VALUE_WORD_ID);
        mFrom = getArguments().getString(KEY_VALUE_FROM);
        mTo = getArguments().getString(KEY_VALUE_TO);
        mIRepository = DictionaryDBRepository.getInstance(getActivity());
        mDictionaryWord = mIRepository.getWord(mWordID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dictionary_detail, container, false);
        fidView(view);
        initView();
        return view;
    }

    private void fidView(View view) {
        mTextViewTitle = view.findViewById(R.id.textView_titleWord);
        mTextViewMeaning = view.findViewById(R.id.textView_meaningWord);
    }

    private void initView() {
        setTextInView(mFrom, mTextViewTitle);
        setTextInView(mTo, mTextViewMeaning);
    }

    private void setTextInView(String state, TextView textView) {
        switch (state) {
            case "Arabic":
                textView.setText(mDictionaryWord.getArabic());
                break;
            case "English":
                textView.setText(mDictionaryWord.getEnglish());
                break;
            case "French":
                textView.setText(mDictionaryWord.getFrench());
                break;
            default:
                textView.setText(mDictionaryWord.getPersian());
                break;
        }
    }
}