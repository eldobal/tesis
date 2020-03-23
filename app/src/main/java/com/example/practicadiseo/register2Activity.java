package com.example.practicadiseo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class register2Activity extends AppCompatActivity {
    SweetAlertDialog dp;
    private SharedPreferences prefs;

    private EditText txtemail;
    private EditText txtcontraseña;
    private EditText txtcontraseña2;
    private EditText txtnombre;
    private EditText txtapellidos;
    private EditText txttelefono;
    private EditText txtcorreo;
    private EditText txtdireccion;
    private Button btnregistrar;
    private int posicion = 0;
   // private List<Usuario> listausuarios;
    //private Usuario usuario;
    //private int tipousuario;
    //private int estadousuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        //listausuarios = new ArrayList<Usuario>();

        txtemail = (EditText) findViewById(R.id.email);
        txtcontraseña = (EditText) findViewById(R.id.password);
        txtcontraseña2 = (EditText) findViewById(R.id.password2);
        txtnombre = (EditText) findViewById(R.id.nombre);
        txtapellidos = (EditText) findViewById(R.id.apellido);
        txttelefono = (EditText) findViewById(R.id.telefono);
        //txtcorreo = (EditText) findViewById(R.id.txtcorreo);

       // txtciudad = (EditText) findViewById(R.id.c);
        btnregistrar=(Button)findViewById(R.id.registrarse);


        btnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temail = txtemail.getText().toString();
                String tcontrasena = txtcontraseña.getText().toString();
                String tcontrasena2 =txtcontraseña2.getText().toString();
                String tnombre = txtnombre.getText().toString();
                String tapellidos = txtapellidos.getText().toString();
                String  tfono = txttelefono.getText().toString();
                String tcorreo =txtcorreo.getText().toString();

                String tdireccion = txtdireccion.getText().toString();


                if(tcontrasena.equals(tcontrasena2)){
                    //se envian parametros al metodo para que puedan realizar las validaciones
                    if (  validaciones(temail,tcontrasena,tnombre,tapellidos,tfono,tcorreo,tdireccion) == true){
                       // enviarRequest(tusuario,tcontrasena,tnombre,tapellidos,tfono,tcorreo,tdireccion);
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean validaciones(String temail,String tcontrasena,String tnombre,String tapellidos,String tfono,String tcorreo,String tdireccion){
        if (!temail.isEmpty() && !tcontrasena.isEmpty() && !tnombre.isEmpty() && !tapellidos.isEmpty() && !tcorreo.isEmpty() && !tdireccion.isEmpty() && !tfono.isEmpty()) {
            if (tfono.length() == 9) {
                return true;
            } else {
                Toast.makeText(getApplicationContext(), "Inserte 9 digitos en telefono ", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return false;
    }

    private String getuserusuairoprefs(){

        return prefs.getString("usuario","");
    }

}
