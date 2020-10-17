package org.maktab.dictionary.controller.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;

import org.maktab.dictionary.R;
import org.maktab.dictionary.model.DictionaryWord;
import org.maktab.dictionary.repository.DictionaryDBRepository;
import org.maktab.dictionary.repository.IRepository;

public class DeleteFragment extends DialogFragment {
    public static final String KEY_VALUE_WORD_ID = "key_value_wordId";
    private IRepository mIRepository;
    private long mWordId;
    private DictionaryWord mDictionaryWord;

    public DeleteFragment() {
        // Required empty public constructor
    }

    public static DeleteFragment newInstance(long wordId) {
        DeleteFragment fragment = new DeleteFragment();
        Bundle args = new Bundle();
        args.putLong(KEY_VALUE_WORD_ID,wordId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIRepository = DictionaryDBRepository.getInstance(getActivity());
        mWordId = getArguments().getLong(KEY_VALUE_WORD_ID);
        mDictionaryWord = mIRepository.getWord(mWordId);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_delete, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.delete_word);
        builder.setIcon(R.drawable.ic_error);
        builder.setView(view);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mIRepository.deleteWord(mDictionaryWord);
                sendResult();

            }
        })
                .setNegativeButton(R.string.no, null);


        AlertDialog dialog = builder.create();
        return dialog;
    }

    private void sendResult() {
        Fragment fragment = getTargetFragment();
        int requestCode = getTargetRequestCode();
        int resultCode = Activity.RESULT_OK;
        Intent intent = new Intent();

        fragment.onActivityResult(requestCode, resultCode, intent);
    }
}