package com.example.practicadiseo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.practicadiseo.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class settingsFragment extends Fragment {

    SweetAlertDialog dp;
    private SharedPreferences prefs;
    public settingsFragment() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        View v= inflater.inflate(R.layout.fragment_settings, container, false);

        //declaracion de botones
        final Button btnsalir = (Button) v.findViewById(R.id.btnsalir);
        final Button btnpreguntas = (Button) v.findViewById(R.id.btnpreguntas);


        //boton que te redirije al fragment de preguntas
        btnpreguntas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectedFragment(new preguntasFragment());
            }
        });

        //borra las prefs y sale de la sesion
        btnsalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dp=  new SweetAlertDialog(v.getContext(), SweetAlertDialog.WARNING_TYPE);
                dp.setTitleText("Esta Seguro?");
                dp.setContentText("Se cerrará la sesion y se eliminaran los datos de inicio!");
                dp.setCancelText("No");
                dp.setConfirmText("Si");
                dp.showCancelButton(true);
                dp.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        //codigo para cuando se preciona si
                        removesharedpreferenced();
                        logout();
                    }
                });
                dp.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                        .show();
            }
        });



        return v;

    }


    //metodo que te dirije al login /no te deja volver con el boton
    public void logout(){
        Intent intent= new Intent(getView().getContext(),login2Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity(intent);
    }


    //metodo para poder pasar de un fragmen a otro /(en caso de que no funcione proba getchild fragment)
    private void showSelectedFragment(Fragment fragment){
        getFragmentManager().beginTransaction().replace(R.id.container,fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();

    }



    //metodo para borrar las credenciales guardadas
    public  void removesharedpreferenced(){
        prefs.edit().clear().apply();
    }



}
