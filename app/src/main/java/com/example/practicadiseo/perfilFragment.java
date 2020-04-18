package com.example.practicadiseo;

import android.content.Intent;
import android.icu.text.RelativeDateTimeFormatter;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.method.KeyListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.practicadiseo.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class perfilFragment extends Fragment {

    private ImageView fotoperfil;
    SweetAlertDialog dp;
    private GoogleSignInClient googleSignInClient;
    public perfilFragment() {
        // Required empty public constructor


    }

    public void onCreate (Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);



    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate        the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_perfil, container, false);

        //declaracion de variables
        final TextView rut = (EditText) v.findViewById(R.id.rut);
        final TextView nombre = (EditText) v.findViewById(R.id.nombre);
        final TextView apellido = (EditText) v.findViewById(R.id.apellido);
        final TextView correo = (EditText) v.findViewById(R.id.email);
        final TextView telefono = (EditText) v.findViewById(R.id.telefono);
        final Spinner ciudad = (Spinner) v.findViewById(R.id.spinner);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        fotoperfil = (ImageView) v.findViewById(R.id.usericon);

        //trozo de codigo para rescatar parametros de la cuenta de usuario
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            nombre.setText(personGivenName);
            apellido.setText(personFamilyName);
            correo.setText(personEmail);
            Glide.with(this).load(String.valueOf(personPhoto)).into(fotoperfil);
            Toast.makeText(getContext(), "Nombre"+personFamilyName+" Correo: "+personEmail+ " id:" +personId+"", Toast.LENGTH_LONG).show();
        }





        //seccion de codigo en el cual se debera traer el json con los datos del usuario
        //donde se setearan los datos a los edittext

        final Button editardatos = (Button) v.findViewById(R.id.actualizarperfil);
        final Button editarpass = (Button) v.findViewById(R.id.actualizarcontraseña);





        //cuando se apriete el boton se preguntara si desea editar
        //lo cual hara los edittext editables otra vez
        //si el usuario vuevle a apretar los datos devera guardar y volver a poner no editable los edittext
        editardatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dp =new SweetAlertDialog(v.getContext(), SweetAlertDialog.WARNING_TYPE);
                        dp.setTitleText("Estas Segur@ De Querer Cambiar Los Datos?");
                        dp.setContentText("Podras Cambiar Tus Datos Personales!");
                        dp.setConfirmText("Si,Deseo Actualizar!");

                        dp.setCancelText("No,No Quiero");
                        //si preciona el boton si se podran editar los edittext
                        dp.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {


                            @Override
                            public void onClick(final SweetAlertDialog sDialog) {
                                //si vuelve a precionar el boton no podra editar los edittext y saldra un mensaje
                                editardatos.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        dp= new SweetAlertDialog(v.getContext(), SweetAlertDialog.SUCCESS_TYPE);
                                        dp.setTitleText("Has Actualizado tu perfil !");
                                        dp.setContentText("para volver a editar recargue el perfil!");
                                        dp.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sDialog.dismissWithAnimation();
                                                        //metodo para cambiar de activity
                                                        updateDetail();

                                                    }
                                                })
                                                .show();

                                        //parametros false
                                        { rut.setEnabled(false);
                                            rut.setFocusable(false);
                                            rut.setFocusableInTouchMode(false);

                                            correo.setEnabled(false);
                                            correo.setFocusable(false);
                                            correo.setFocusableInTouchMode(false);

                                            nombre.setEnabled(false);
                                            nombre.setFocusable(false);
                                            nombre.setFocusableInTouchMode(false);

                                            apellido.setEnabled(false);
                                            apellido.setFocusable(false);
                                            apellido.setFocusableInTouchMode(false);

                                            telefono.setEnabled(false);
                                            telefono.setFocusable(false);
                                            telefono.setFocusableInTouchMode(false);

                                            ciudad.setEnabled(false);
                                            ciudad.setFocusable(false);
                                            ciudad.setFocusableInTouchMode(false); }


                                    }
                                });
                                sDialog.dismissWithAnimation();

                                rut.setEnabled(true);
                                rut.setFocusable(true);
                                rut.setFocusableInTouchMode(true);
                                rut.setText(rut.getText());

                                correo.setEnabled(true);
                                correo.setFocusable(true);
                                correo.setFocusableInTouchMode(true);
                                correo.setText(correo.getText());

                                nombre.setEnabled(true);
                                nombre.setFocusable(true);
                                nombre.setFocusableInTouchMode(true);
                                nombre.setText(nombre.getText());

                                apellido.setEnabled(true);
                                apellido.setFocusable(true);
                                apellido.setFocusableInTouchMode(true);
                                apellido.setText(apellido.getText());

                                telefono.setEnabled(true);
                                telefono.setFocusable(true);
                                telefono.setFocusableInTouchMode(true);
                                telefono.setText(telefono.getText());

                                ciudad.setEnabled(true);
                                ciudad.setFocusable(true);
                                ciudad.setFocusableInTouchMode(true);


                            }
                        })
                        .show();
            }
        });


        editarpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectedFragment(new passperfilFragment());
            }
        });

        return v;

    }
    //ir desde un fragment hacia una actividad
    public void updateDetail() {
        Intent intent = new Intent(getActivity(), menuActivity.class);
        startActivity(intent);
    }


    private void showSelectedFragment(Fragment fragment){
        getFragmentManager().beginTransaction().replace(R.id.container,fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }


}
