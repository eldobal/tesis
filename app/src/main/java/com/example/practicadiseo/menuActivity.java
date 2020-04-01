package com.example.practicadiseo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.practicadiseo.fragments.HomeFragment;
import com.example.practicadiseo.fragments.settingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class menuActivity extends AppCompatActivity {
    BottomNavigationView mbottomNavigationView;
    SweetAlertDialog dp;

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
                    showSelectedFragment(new solicitudeFragment());

                }

                if(menuItem.getItemId()== R.id.menu_settings){
                    showSelectedFragment(new settingsFragment());
                }
                return true;
            }
        });



    }



    //metodo que permite elejir un fragment
    private void showSelectedFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();

    }
}
