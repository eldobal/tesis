package com.example.practicadiseo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class menuActivity extends AppCompatActivity {


    BottomNavigationView mbottomNavigationView;
    SweetAlertDialog dp;
    int contador=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //con este metodo selecciono el fragment de inicio por defecto
        showSelectedFragment(new HomeFragment());



        mbottomNavigationView=(BottomNavigationView) findViewById(R.id.bottomnavigation);

        mbottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if(menuItem.getItemId()== R.id.menu_profile){
                    showSelectedFragment(new perfilFragment());

                }

                if(menuItem.getItemId()== R.id.menu_home){
                    showSelectedFragment(new HomeFragment());

                }

                if(menuItem.getItemId()==R.id.menu_solicitud){
                    showSelectedFragment(new DetalleSolicitudFragment());

                }

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
}
