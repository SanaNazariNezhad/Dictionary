package org.maktab.dictionary.controller.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.maktab.dictionary.R;

public class DictionaryDetailFragment extends Fragment {

    public DictionaryDetailFragment() {
        // Required empty public constructor
    }

    public static DictionaryDetailFragment newInstance() {
        DictionaryDetailFragment fragment = new DictionaryDetailFragment();
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
        View view = inflater.inflate(R.layout.fragment_dictionary_detail, container, false);

        return view;
    }
}