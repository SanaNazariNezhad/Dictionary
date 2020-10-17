package org.maktab.dictionary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.maktab.dictionary.model.DictionaryWord;
import org.maktab.dictionary.repository.DictionaryDBRepository;
import org.maktab.dictionary.repository.IRepository;

import java.util.Objects;

public class InsertFragment extends DialogFragment {
    private TextInputEditText mEditTextArabic, mEditTextEnglish, mEditTextFrench, mEditTextPersian;
    private TextInputLayout mArabicForm, mEnglishForm, mFrenchForm, mPersianForm;
    private Button mButtonCancel, mButtonSave;
    private IRepository mIRepository;

    public InsertFragment() {
        // Required empty public constructor
    }

    public static InsertFragment newInstance() {
        InsertFragment fragment = new InsertFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIRepository = DictionaryDBRepository.getInstance(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_insert, container, false);
        findView(view);
        listeners();
        return view;
    }

    private void listeners() {
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInput()) {
                    sendResult();
                    dismiss();
                } else {
                    checkInput();
                }
            }
        });
    }

    private void checkInput() {
        mArabicForm.setErrorEnabled(false);
        mEnglishForm.setErrorEnabled(false);
        mFrenchForm.setErrorEnabled(false);
        mPersianForm.setErrorEnabled(false);
        if (mEditTextArabic.getText().toString().trim().isEmpty()) {
            mArabicForm.setErrorEnabled(true);
            mArabicForm.setError("Field cannot be empty!");
        }
        if (mEditTextEnglish.getText().toString().trim().isEmpty()) {
            mEnglishForm.setErrorEnabled(true);
            mEnglishForm.setError("Field cannot be empty!");
        }
        if (mEditTextFrench.getText().toString().trim().isEmpty()) {
            mFrenchForm.setErrorEnabled(true);
            mFrenchForm.setError("Field cannot be empty!");
        }
        if (mEditTextPersian.getText().toString().trim().isEmpty()) {
            mPersianForm.setErrorEnabled(true);
            mPersianForm.setError("Field cannot be empty!");
        }
    }

    private boolean validateInput() {
        return !mEditTextArabic.getText().toString().trim().isEmpty() &&
                !mEditTextEnglish.getText().toString().trim().isEmpty() &&
                !mEditTextFrench.getText().toString().trim().isEmpty() &&
                !mEditTextPersian.getText().toString().trim().isEmpty();
    }

    private void sendResult() {
        Fragment fragment = getTargetFragment();
        int requestCode = getTargetRequestCode();
        int resultCode = Activity.RESULT_OK;
        Intent intent = new Intent();
        DictionaryWord dictionaryWord = new DictionaryWord(mEditTextArabic.getText().toString(),
                mEditTextEnglish.getText().toString(), mEditTextFrench.getText().toString(),
                mEditTextPersian.getText().toString());
        mIRepository.insertWord(dictionaryWord);

        fragment.onActivityResult(requestCode, resultCode, intent);
    }

    private void findView(View view) {
        mButtonCancel = view.findViewById(R.id.btn_cancel_insert);
        mButtonSave = view.findViewById(R.id.btn_save_insert);
        mEditTextArabic = view.findViewById(R.id.arabic_insert);
        mEditTextEnglish = view.findViewById(R.id.english_insert);
        mEditTextFrench = view.findViewById(R.id.french_insert);
        mEditTextPersian = view.findViewById(R.id.persian_insert);
        mArabicForm = view.findViewById(R.id.arabic_form_insert);
        mEnglishForm = view.findViewById(R.id.english_form_insert);
        mFrenchForm = view.findViewById(R.id.french_form_insert);
        mPersianForm = view.findViewById(R.id.persian_form_insert);
    }
}