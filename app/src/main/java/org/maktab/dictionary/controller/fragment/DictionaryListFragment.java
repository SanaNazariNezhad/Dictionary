package org.maktab.dictionary.controller.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.maktab.dictionary.R;
import org.maktab.dictionary.controller.activity.DictionaryDetail;
import org.maktab.dictionary.model.DictionaryWord;
import org.maktab.dictionary.repository.DictionaryDBRepository;
import org.maktab.dictionary.repository.IRepository;

import java.util.List;
import java.util.Locale;

public class DictionaryListFragment extends Fragment {
    public static final String FRAGMENT_TAG_INSERT = "Insert";
    public static final int REQUEST_CODE_INSERT = 0;
    private static final int REQUEST_CODE_SETTING = 1;
    public static final String SETTINGS = "Settings";
    public static final String MY_LANG = "My_Lang";
    private RecyclerView mRecyclerView;
    private IRepository mRepository;
    private List<DictionaryWord> mDictionaryWords;
    private DictionaryAdapter mDictionaryAdapter;
    private TextInputEditText mEditTextSearch;
    private ImageView mImageViewSearch;
    private TextView mTextViewFrom, mTextViewTo;
    private TextInputLayout mTextInputLayoutFrom, mTextInputLayoutTo;
    private String mFrom, mTo;
    private String mArabic, mEnglish, mFrench, mPersian;
    private String mStringLanguage;

    public DictionaryListFragment() {
        // Required empty public constructor
    }

    public static DictionaryListFragment newInstance() {
        DictionaryListFragment fragment = new DictionaryListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepository = DictionaryDBRepository.getInstance(getActivity());
        setHasOptionsMenu(true);
        loadLocale();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle(R.string.app_name);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dictionary_list, container, false);
        findView(view);
        initView(view);
        if (savedInstanceState != null) {
            mDictionaryWords = mRepository.getWords();
            search();
            initRecyclerView();
        }
        listeners();
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onResume() {
        super.onResume();
        updateSubtitle();
        search();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_dictionary_list_fragment, menu);
        updateSubtitle();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_search:

                InsertFragment insertFragment = InsertFragment.newInstance();
                insertFragment.setTargetFragment(
                        DictionaryListFragment.this,
                        REQUEST_CODE_INSERT);
                insertFragment.show(
                        getActivity().getSupportFragmentManager(),
                        FRAGMENT_TAG_INSERT);

                return true;

            case R.id.app_bar_setting:
                showChangeLanguageDialog();

                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showChangeLanguageDialog() {
        final String[] listItems = {mEnglish, mPersian};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        mBuilder.setTitle(R.string.select_language);
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    setLocal("en");
                } else if (i == 1) {
                    setLocal("fa");
                }
            }
        });
        mBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        mBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getActivity().recreate();
                dialogInterface.dismiss();
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void setLocal(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getActivity().getBaseContext().getResources().updateConfiguration(config,
                getActivity().getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getActivity().getSharedPreferences(SETTINGS, getActivity().MODE_PRIVATE).edit();
        editor.putString(MY_LANG, lang);
        editor.apply();

    }

    public void loadLocale() {
        SharedPreferences preferences = getActivity().getSharedPreferences(SETTINGS, Activity.MODE_PRIVATE);
        String language = preferences.getString(MY_LANG, "");
        setLocal(language);
        mStringLanguage = language;
        mArabic = getString(R.string.arabic);
        mEnglish = getString(R.string.english);
        mFrench = getString(R.string.french);
        mPersian = getString(R.string.persian);
    }

    private void updateSubtitle() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        IRepository iRepository = DictionaryDBRepository.getInstance(getActivity());
        String subTitle = iRepository.getWords().size() + "";
        activity.getSupportActionBar().setSubtitle(subTitle);
    }

    private void findView(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view_dictionary_list);
        mEditTextSearch = view.findViewById(R.id.search);
        mImageViewSearch = view.findViewById(R.id.search_img);
        mTextViewFrom = view.findViewById(R.id.filled_exposed_dropdown_from);
        mTextViewTo = view.findViewById(R.id.filled_exposed_dropdown_to);
        mTextInputLayoutFrom = view.findViewById(R.id.from_form);
        mTextInputLayoutTo = view.findViewById(R.id.to_form);
    }

    private void initView(View view) {
        exposedDropdownMenus(view, R.id.filled_exposed_dropdown_from);
        exposedDropdownMenus(view, R.id.filled_exposed_dropdown_to);
    }

    private void listeners() {
        mImageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInputLang()) {
                    search();
                    initRecyclerView();
                }
            }
        });
    }

    private boolean checkInputLang() {
        mTextInputLayoutFrom.setErrorEnabled(false);
        mTextInputLayoutTo.setErrorEnabled(false);
        if (mTextViewFrom.getText().toString().trim().isEmpty() && mTextViewTo.getText().toString().trim().isEmpty()) {
            mTextInputLayoutFrom.setErrorEnabled(true);
            mTextInputLayoutFrom.setError("Field cannot be empty!");
            mTextInputLayoutTo.setErrorEnabled(true);
            mTextInputLayoutTo.setError("Field cannot be empty!");
            return false;
        } else if (mTextViewFrom.getText().toString().trim().isEmpty()) {
            mTextInputLayoutFrom.setErrorEnabled(true);
            mTextInputLayoutFrom.setError("Field cannot be empty!");
            return false;
        } else if (mTextViewTo.getText().toString().trim().isEmpty()) {
            mTextInputLayoutTo.setErrorEnabled(true);
            mTextInputLayoutTo.setError("Field cannot be empty!");
            return false;
        }
        return true;
    }

    private void search() {
        String search = "%" + mEditTextSearch.getText() + "%";
        mFrom = mTextViewFrom.getText().toString();
        mTo = mTextViewTo.getText().toString();
        if (mFrom.equals("Arabic") || mFrom.equals("عربی")){
            mDictionaryWords = mRepository.searchArabic(search);
        }else if (mFrom.equals("English") || mFrom.equals("انگلیسی")){
            mDictionaryWords = mRepository.searchEnglish(search);
        }else if (mFrom.equals("French") || mFrom.equals("فرانسوی")){
            mDictionaryWords = mRepository.searchEnglish(search);
        }else if (mFrom.equals("Persian") || mFrom.equals("فارسی")){
            mDictionaryWords = mRepository.searchEnglish(search);
        }
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
    }

    public void updateUI() {
        if (mDictionaryAdapter == null) {
            mDictionaryAdapter = new DictionaryAdapter(mDictionaryWords);
            mRecyclerView.setAdapter(mDictionaryAdapter);
        } else {
            mDictionaryAdapter.setDictionaryWords(mDictionaryWords);
            mDictionaryAdapter.notifyDataSetChanged();
        }
    }

    private void exposedDropdownMenus(View view, int filledExposedDropdown) {
        String[] COUNTRIES = new String[]{mArabic, mEnglish, mFrench, mPersian};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getContext(),
                        R.layout.dropdown_menu_popup_item,
                        COUNTRIES);

        AutoCompleteTextView editTextFilledExposedDropdown =
                view.findViewById(filledExposedDropdown);
        editTextFilledExposedDropdown.setAdapter(adapter);
    }

    private class DictionaryHolder extends RecyclerView.ViewHolder {

        private TextView mTextViewWord;
        private TextView mTextViewMeaning;
        private DictionaryWord mDictionaryWord;

        public DictionaryHolder(@NonNull View itemView) {
            super(itemView);

            mTextViewWord = itemView.findViewById(R.id.row_item_textView_word);
            mTextViewMeaning = itemView.findViewById(R.id.row_item_textView_meaning);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent dictionaryDetailIntent = DictionaryDetail.newIntent(getActivity(),
                            mDictionaryWord.getPrimaryId(), mFrom, mTo);
                    startActivity(dictionaryDetailIntent);

                }
            });
        }

        public void bindCrime(DictionaryWord dictionaryWord) {
            mDictionaryWord = dictionaryWord;
            showWord(dictionaryWord, mFrom, mTextViewWord);
            showWord(dictionaryWord, mTo, mTextViewMeaning);
        }

        private void showWord(DictionaryWord dictionaryWord, String state, TextView textViewWord) {
            if (state.equals("Arabic") || state.equals("عربی")){
                textViewWord.setText(dictionaryWord.getArabic());
            }else if (state.equals("English") || state.equals("انگلیسی")){
                textViewWord.setText(dictionaryWord.getEnglish());
            }else if (state.equals("French") || state.equals("فرانسوی")){
                textViewWord.setText(dictionaryWord.getFrench());
            }else if (state.equals("Persian") || state.equals("فارسی")){
                textViewWord.setText(dictionaryWord.getPersian());
            }
        }
    }

    private class DictionaryAdapter extends RecyclerView.Adapter<DictionaryHolder> {

        private List<DictionaryWord> mDictionaryWords;

        public List<DictionaryWord> getDictionaryWords() {
            return mDictionaryWords;
        }

        public void setDictionaryWords(List<DictionaryWord> dictionaryWords) {
            mDictionaryWords = dictionaryWords;
        }

        public DictionaryAdapter(List<DictionaryWord> dictionaryWords) {
            mDictionaryWords = dictionaryWords;
        }

        @Override
        public int getItemCount() {
            return mDictionaryWords.size();
        }

        @NonNull
        @Override
        public DictionaryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.dictionary_row_list, parent, false);
            DictionaryHolder dictionaryHolder = new DictionaryHolder(view);
            return dictionaryHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull DictionaryHolder holder, int position) {

            DictionaryWord dictionaryWord = mDictionaryWords.get(position);
            holder.bindCrime(dictionaryWord);
        }
    }

}