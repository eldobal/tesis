package com.example.practicadiseo;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.practicadiseo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener{
    private CardView belleza,salud;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_home, container, false);
        //declaracion de cardview
        CardView belleza = (CardView) v.findViewById(R.id.belleza);
        CardView salud = (CardView) v.findViewById(R.id.salud);

        //añadiendo click listener a las cards
        belleza.setOnClickListener(this);
        salud.setOnClickListener(this);


        return v;
    }
    public void onClick(View v){
        Intent i;

        switch (v.getId()){
            case R.id.belleza : i = new Intent(this,areabellezaFragment.class);startActivity(i); ; break ;
        }

    }


}
