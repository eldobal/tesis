package com.example.practicadiseo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class menuareasActivity extends AppCompatActivity {
    ImageButton botonconf;
    private SharedPreferences prefs;
    SweetAlertDialog dp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuareas);
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        setcredentiasexist();




    }

    //metodo para accionar el salir sesion
    public void logout(){
        Intent intent= new Intent(menuareasActivity.this,login2Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity(intent);
    }

    //compovar que exista algo guadado en las credenciales
    private void setcredentiasexist(){
        String usuario = getuserusuairoprefs();

        if(!TextUtils.isEmpty(usuario)){
           // nombreusuario.setText(usuario);

        }
    }

    //metodo para borrar las credenciales guardadas
    public  void removesharedpreferenced(){
        prefs.edit().clear().apply();
    }

    //recupera el usuario guardado
    private String getuserusuairoprefs(){

        return prefs.getString("Usuario","");
    }


}
