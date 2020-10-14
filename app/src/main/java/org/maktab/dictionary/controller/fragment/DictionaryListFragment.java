package org.maktab.dictionary.controller.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.maktab.dictionary.R;
import org.maktab.dictionary.model.DictionaryWord;
import org.maktab.dictionary.repository.DictionaryDBRepository;
import org.maktab.dictionary.repository.IRepository;

import java.util.List;

public class DictionaryListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private IRepository mRepository;
    private List<DictionaryWord> mDictionaryWords;
    private DictionaryAdapter mDictionaryAdapter;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dictionary_list, container, false);
        findView(view);
        initView(view);
        return view;
    }

    private void findView(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view_dictionary_list);
    }

    private void initView(View view) {
        exposedDropdownMenus(view, R.id.filled_exposed_dropdown_from);
        exposedDropdownMenus(view, R.id.filled_exposed_dropdown_to);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
    }

    public void updateUI() {
        mDictionaryWords = mRepository.getWords();
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

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallbacks.onCrimeSelected(mCrime);

                }
            });*/
        }

        public void bindCrime(DictionaryWord dictionaryWord) {
            mDictionaryWord = dictionaryWord;
            mTextViewWord.setText(dictionaryWord.getEnglish());
            mTextViewMeaning.setText(dictionaryWord.getPersian());
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