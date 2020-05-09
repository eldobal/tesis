package com.example.practicadiseo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class menuActivity extends AppCompatActivity {
    private GoogleSignInClient googleSignInClient;
    TextView nombre,email,id;
    ImageView fotoperfil;
    BottomNavigationView mbottomNavigationView;
    SweetAlertDialog dp;
    private SharedPreferences prefs;
    int contador=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //al momento de crear el home en el onCreate cargar con el metodo sin backtostack
        //cargarfragment(new HomeFragment());
        //cargarfragment(new perfilFragment());
        //cargarfragment(new solicitudeFragment());
        //cargarfragment(new settingsFragment());
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        //se instancia el gso
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // trae el cliende de google
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        //trozo de codigo para rescatar parametros de la cuenta de usuario
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            Toast.makeText(menuActivity.this, "Nombre"+personName+" Correo: "+personEmail+ " id:" +personId+"", Toast.LENGTH_LONG).show();
        }
        mbottomNavigationView=(BottomNavigationView) findViewById(R.id.bottomnavigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();


        mbottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //se muestra el fragment de peril
                if(menuItem.getItemId()== R.id.menu_profile){
                    showSelectedFragment(new perfilFragment());

                }
                //se muestra el fragment de rubros
                if(menuItem.getItemId()== R.id.menu_home){
                    showSelectedFragment(new HomeFragment());
                }
                //se muestra el fragment de la lista de solicitudes
                if(menuItem.getItemId()==R.id.menu_solicitud){
                    showSelectedFragment(new solicitudeFragment());
                }
                //se muestra el fragment de configuracion y setting
                if(menuItem.getItemId()== R.id.menu_settings){
                    showSelectedFragment(new settingsFragment());
                }
                return true;
            }
        });
    }


    //metodo para que cuando el usuario este en la actividad principal y quiera retrocer tenga que apretar dos veces el back
    @Override
    public void onBackPressed() {

        if (contador==0){
            Toast.makeText(menuActivity.this, "Precione nuevamente para salir", Toast.LENGTH_LONG).show();
            contador++;
        }else{
            super.onBackPressed();
        }
        new CountDownTimer(3000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                contador=0;
            }
        }.start();
    }






    //metodo que permite elejir un fragment
    private void showSelectedFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                //permite regresar hacia atras entre los fragments
                //.addToBackStack(null)
                .commit();
    }


    //metodo que permite elejir un fragment y no volver hacia atras
    private void cargarfragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().add(R.id.container,fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                //permite regresar hacia atras entre los fragments
                .addToBackStack(null)
                .commit();
    }

    private void saveOnPreferences(String rut, String contrasena) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Rut", rut);
        editor.putString("ContraseNa", contrasena);
        //linea la cual guarda todos los valores en la pref antes de continuar
        editor.commit();
        editor.apply();
    }

    private void setcredentiasexist() {
        String rut = getuserrutprefs();
        String contraseña = getusercontraseñaprefs();
        if (!TextUtils.isEmpty(rut) && !TextUtils.isEmpty(contraseña)) {
          //  txtrut.setText(rut);
          //  txtpass.setText(contraseña);
        }
    }


    private String getuserrutprefs() {
        return prefs.getString("Rut", "");
    }

    private String getusercontraseñaprefs() {
        return prefs.getString("ContraseNa", "");
    }


}
