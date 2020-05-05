package com.example.practicadiseo;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class areabellezaFragment extends Fragment {
CardView cardpeluqueria,cardmanicurista,cardpodologa,cardbarbero,cardmaquilladora,cardmasajista;
    public areabellezaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_areabelleza, container, false);

        cardpeluqueria =(CardView) v.findViewById(R.id.cardpeluquera);
        cardmanicurista =(CardView) v.findViewById(R.id.cardmanicurista);
        cardpodologa =(CardView) v.findViewById(R.id.cardpodologa);
        cardbarbero=(CardView) v.findViewById(R.id.cardbarbero);
        cardmaquilladora =(CardView) v.findViewById(R.id.cardmaquilladora);
        cardmasajista =(CardView) v.findViewById(R.id.cardmasajista);

        cardpeluqueria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idrubro = 1;
                listabuscarrubroFragment listabuscarrubroFragment = new listabuscarrubroFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("idRubro", idrubro);
                listabuscarrubroFragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, listabuscarrubroFragment);
                fragmentTransaction.addToBackStack(null);
                //Terminar transición y nos vemos en el fragmento de destino
                fragmentTransaction.commit();
            }
        });

        cardmanicurista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //metodo para que se envie el id del rubro el cual sera estatico para enviar a la lista de trabajadores
                int idrubro = 2;
                listabuscarrubroFragment listabuscarrubroFragment = new listabuscarrubroFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("idRubro", idrubro);
                listabuscarrubroFragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, listabuscarrubroFragment);
                fragmentTransaction.addToBackStack(null);
                // Terminar transición y nos vemos en el fragmento de destino
                fragmentTransaction.commit();
            }
        });

        cardpodologa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //metodo para que se envie el id del rubro el cual sera estatico para enviar a la lista de trabajadores
                int idrubro = 3;
                listabuscarrubroFragment listabuscarrubroFragment = new listabuscarrubroFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("idRubro", idrubro);
                listabuscarrubroFragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, listabuscarrubroFragment);
                fragmentTransaction.addToBackStack(null);
                // Terminar transición y nos vemos en el fragmento de destino
                fragmentTransaction.commit();
            }
        });

        cardbarbero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //metodo para que se envie el id del rubro el cual sera estatico para enviar a la lista de trabajadores
                int idrubro = 4;
                listabuscarrubroFragment listabuscarrubroFragment = new listabuscarrubroFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("idRubro", idrubro);
                listabuscarrubroFragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, listabuscarrubroFragment);
                fragmentTransaction.addToBackStack(null);
                // Terminar transición y nos vemos en el fragmento de destino
                fragmentTransaction.commit();
            }
        });

        cardmaquilladora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //metodo para que se envie el id del rubro el cual sera estatico para enviar a la lista de trabajadores
                int idrubro = 5;
                listabuscarrubroFragment listabuscarrubroFragment = new listabuscarrubroFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("idRubro", idrubro);
                listabuscarrubroFragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, listabuscarrubroFragment);
                fragmentTransaction.addToBackStack(null);
                // Terminar transición y nos vemos en el fragmento de destino
                fragmentTransaction.commit();
            }
        });

        cardmasajista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //metodo para que se envie el id del rubro el cual sera estatico para enviar a la lista de trabajadores
                int idrubro = 6;
                listabuscarrubroFragment listabuscarrubroFragment = new listabuscarrubroFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("idRubro", idrubro);
                listabuscarrubroFragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, listabuscarrubroFragment);
                fragmentTransaction.addToBackStack(null);
                // Terminar transición y nos vemos en el fragmento de destino
                fragmentTransaction.commit();
            }
        });

        return  v;
    }
}
