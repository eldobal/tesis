package com.example.practicadiseo.fragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.practicadiseo.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class areabellezaFragment extends Fragment {
CardView cardpeluqueria,cardmanicurista,cardbarbero,cardmaquilladora;
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
        cardbarbero=(CardView) v.findViewById(R.id.cardbarbero);
        cardmaquilladora =(CardView) v.findViewById(R.id.cardmaquilladora);


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

        return  v;
    }
}
