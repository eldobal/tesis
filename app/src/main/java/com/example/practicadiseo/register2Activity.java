package com.example.practicadiseo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class register2Activity extends AppCompatActivity {
    SweetAlertDialog dp;
    private SharedPreferences prefs;
    private ArrayAdapter<Ciudad> adapter;
    private EditText txtrut;
    private EditText txtemail;
    private EditText txtcontraseña;
    private EditText txtcontraseña2;
    private EditText txtnombre;
    private EditText txtapellidos;
    private EditText txttelefono;
    private EditText txtcorreo;
    private EditText txtdireccion;
    private Spinner spinnerciudades;
    private Button btnregistrar;
    private int posicion = 0;
    private List<Ciudad> listaciudades;
    //private Usuario usuario;
    //private int tipousuario;
    //private int estadousuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        //listausuarios = new ArrayList<Usuario>();
        txtrut = (EditText) findViewById(R.id.rut) ;
        txtemail = (EditText) findViewById(R.id.email);
        txtcontraseña = (EditText) findViewById(R.id.password);
        txtcontraseña2 = (EditText) findViewById(R.id.password2);
        txtnombre = (EditText) findViewById(R.id.nombre);
        txtapellidos = (EditText) findViewById(R.id.apellido);
        txttelefono = (EditText) findViewById(R.id.telefono);
        //txtcorreo = (EditText) findViewById(R.id.txtcorreo);

        spinnerciudades = (Spinner) findViewById(R.id.spinner);
        btnregistrar=(Button)findViewById(R.id.registrarse);


        cargarspiner();






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

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(register2Activity.this, login2Activity.class);
        startActivity(intent);

        super.onBackPressed();
    }


    private void cargarspiner(){
        try {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://proyectotesis.ddns.net/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            tesisAPI tesisAPI = retrofit.create(com.example.practicadiseo.tesisAPI.class);

            //metodo para llamar a la funcion que queramos
            Call<List<Ciudad>> call = tesisAPI.getCiudades();
            call.enqueue(new Callback<List<Ciudad>>() {
                @Override
                public void onResponse( Call<List<Ciudad>>call, Response<List<Ciudad>>response) {

                    //si esta malo se ejecuta este trozo
                    if(!response.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "error :"+response.code(), Toast.LENGTH_LONG).show();
                    }
                    //de lo contrario se ejecuta esta parte
                    else {
                        //respuesta del request
                        List<Ciudad> ciudades = response.body();

                        //declaracion de variables del response
                        for(Ciudad ciudad : ciudades){
                            //falta poder cargar la lista del response hacia un spinner

                            //agregar los objetos ciudad de la lista del response a una lista
                            listaciudades.add(ciudad);
                        }
                        //cargar la lista de ciudades rescatadas en el spinner
                        ArrayAdapter<Ciudad> a = new ArrayAdapter<Ciudad>(register2Activity.this,android.R.layout.simple_spinner_dropdown_item,listaciudades);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerciudades.setAdapter(a);

                    }
                }

                //si falla el request a la pagina mostrara este error
                @Override
                public void onFailure(Call<List<Ciudad>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "error :"+t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }


    }


}
