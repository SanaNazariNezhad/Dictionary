package org.maktab.dictionary.controller.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

import org.maktab.dictionary.R;
import org.maktab.dictionary.controller.activity.DictionaryDetail;
import org.maktab.dictionary.model.DictionaryWord;
import org.maktab.dictionary.repository.DictionaryDBRepository;
import org.maktab.dictionary.repository.IRepository;

import java.util.List;

public class DictionaryListFragment extends Fragment {
    public static final String FRAGMENT_TAG_INSERT = "Insert";
    public static final int REQUEST_CODE_INSERT = 0;
    private RecyclerView mRecyclerView;
    private IRepository mRepository;
    private List<DictionaryWord> mDictionaryWords;
    private DictionaryAdapter mDictionaryAdapter;
    private TextInputEditText mEditTextSearch;
    private ImageView mImageViewSearch;
    private TextView mTextViewFrom, mTextViewTo;
    private String mFrom,mTo;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dictionary_list, container, false);
        findView(view);
        initView(view);
        if (savedInstanceState != null){
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
        inflater.inflate(R.menu.menu_dictionary_list_fragment,menu);
        updateSubtitle();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.app_bar_search:
                InsertFragment insertFragment = InsertFragment.newInstance();

                insertFragment.setTargetFragment(
                        DictionaryListFragment.this,
                        REQUEST_CODE_INSERT);

                insertFragment.show(
                        getActivity().getSupportFragmentManager(),
                        FRAGMENT_TAG_INSERT);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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
    }

    private void initView(View view) {
        exposedDropdownMenus(view, R.id.filled_exposed_dropdown_from);
        exposedDropdownMenus(view, R.id.filled_exposed_dropdown_to);
    }

    private void listeners() {
        mImageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
                initRecyclerView();
            }
        });
    }

    private void search() {
        String search = "%" + mEditTextSearch.getText() + "%";
        mFrom = mTextViewFrom.getText().toString();
        mTo = mTextViewTo.getText().toString();
        switch (mFrom) {
            case "Arabic":
                mDictionaryWords = mRepository.searchArabic(search);
                break;
            case "English":
                mDictionaryWords = mRepository.searchEnglish(search);
                break;
            case "French":
                mDictionaryWords = mRepository.searchFrench(search);
                break;
            default:
                mDictionaryWords = mRepository.searchPersian(search);
                break;
        }
    }

    private void initRecyclerView(){
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
        String[] COUNTRIES = new String[]{"Arabic", "English", "French", "Persian"};

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
                            mDictionaryWord.getPrimaryId(),mFrom,mTo);
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
            switch (state) {
                case "Arabic":
                    textViewWord.setText(dictionaryWord.getArabic());
                    break;
                case "English":
                    textViewWord.setText(dictionaryWord.getEnglish());
                    break;
                case "French":
                    textViewWord.setText(dictionaryWord.getFrench());
                    break;
                default:
                    textViewWord.setText(dictionaryWord.getPersian());
                    break;
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