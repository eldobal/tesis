package com.example.practicadiseo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.practicadiseo.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class settingsFragment extends Fragment {
    private GoogleSignInClient googleSignInClient;
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


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        googleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        prefs = this.getActivity().getSharedPreferences("Preferences",Context.MODE_PRIVATE);

        //trozo de codigo para rescatar parametros de la cuenta de usuario
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            Toast.makeText(getContext(), "Nombre"+personFamilyName+" Correo: "+personEmail+ " id:" +personId+"", Toast.LENGTH_LONG).show();
        }


        //botton salir de la app
            btnsalir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.btnsalir:
                          SweetAlertDialog dp3 =  new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
                            dp3.setTitleText("Estas seguro de Querer Cerrar Tu Sesion?");
                            dp3.setContentText("Tendras que iniciar sesion para volver a ingrar!");
                            dp3.setConfirmText("Si!");
                            dp3.setCancelText("No!");
                            dp3.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            signOut();
                                            sDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                            break;
                    }
                }
            });



        //boton que te redirije al fragment de preguntas
        btnpreguntas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectedFragment(new preguntasFragment());
            }
        });
        return v;
    }



    //metodo el cual se llama cuando se apreta cerrar sesion
    private void signOut() {
        googleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), task -> {
                    Toast.makeText(getContext(), "Salido", Toast.LENGTH_LONG).show();
                    revokeAccess();
                });
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
                .addToBackStack(null)
                .commit();

    }


    private void revokeAccess() {
        googleSignInClient.revokeAccess()
                .addOnCompleteListener(getActivity(), task -> {
                    //envio hacia el login
                    Intent intent = new Intent(getView().getContext(), login2Activity.class);
                    //flags para que no pueda volver hacia atras
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    //borra al usuario cargado en prefs
                    removesharedpreferenced();
                    startActivity(intent);
                });

    }


    //metodo para borrar las credenciales guardadas
    public  void removesharedpreferenced(){
        prefs.edit().clear().apply();
    }



}
