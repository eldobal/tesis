package com.example.practicadiseo.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.practicadiseo.R;
import com.example.practicadiseo.fragments.areabellezaFragment;
import com.example.practicadiseo.fragments.areasaludFragment;
import com.example.practicadiseo.fragments.areatecnologiaFragment;

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
        CardView tecnologia = (CardView) v.findViewById(R.id.tecnologia);
        //añadiendo click listener a las cards
        belleza.setOnClickListener(this);
        salud.setOnClickListener(this);
        tecnologia.setOnClickListener(this);

        return v;
    }

    //metodo para que al hacer click en un cardview realice una accion que queramos
    public void onClick(View v){
        Intent i;
        //carga los distintos fragments dependiendo del area que escoja el usuario
        switch (v.getId()){
            case R.id.belleza : showSelectedFragment(new areabellezaFragment()); ;break ;
            case R.id.salud : showSelectedFragment(new areasaludFragment()); ; break ;
            case R.id.tecnologia : showSelectedFragment(new areatecnologiaFragment()); ; break ;
            default:break;
        }

    }
    //metodo para poder pasar de un fragmen a otro /(en caso de que no funcione proba getchild fragment)
    private void showSelectedFragment(Fragment fragment){
        getFragmentManager().beginTransaction().replace(R.id.container,fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

}
