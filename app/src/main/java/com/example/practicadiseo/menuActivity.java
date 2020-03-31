package com.example.practicadiseo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class menuActivity extends AppCompatActivity {
    BottomNavigationView mbottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mbottomNavigationView=(BottomNavigationView) findViewById(R.id.bottomnavigation);
        mbottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if(menuItem.getItemId()== R.id.menu_profile){

                }
                if(menuItem.getItemId()== R.id.menu_home){

                }
                if(menuItem.getItemId()== R.id.menu_settings){

                }
                return true;
            }
        });
    }
}
