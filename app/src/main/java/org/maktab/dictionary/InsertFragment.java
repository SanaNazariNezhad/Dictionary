package org.maktab.dictionary;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

public class InsertFragment extends DialogFragment {
    private TextInputEditText mEditTextArabic,mEditTextEnglish,mEditTextFrench,mEditTextPersian;
    private Button mButtonCancel,mButtonSave;

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
    }

    private void findView(View view) {
        mButtonCancel = view.findViewById(R.id.btn_cancel_insert);
        mButtonSave = view.findViewById(R.id.btn_save_insert);
        mEditTextArabic = view.findViewById(R.id.arabic_insert);
        mEditTextEnglish = view.findViewById(R.id.english_insert);
        mEditTextFrench = view.findViewById(R.id.french_insert);
        mEditTextPersian = view.findViewById(R.id.persian_insert);
    }
}