package com.example.practicadiseo.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.practicadiseo.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class areasaludFragment extends Fragment {

    public areasaludFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_areasalud, container, false);
    }
}