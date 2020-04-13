package com.example.practicadiseo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class login2Activity extends AppCompatActivity {



    SweetAlertDialog dp;
    private SharedPreferences prefs;
    private EditText txtrut;
    private EditText txtpass;
    private Button btnlogin;
    private Button btnregister;

    public login2Activity() {
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);





        txtrut = (EditText) findViewById(R.id.txtemail);
        txtpass = (EditText) findViewById(R.id.txtpassword);
        btnlogin = (Button) findViewById(R.id.btnlogin);
        btnregister = (Button) findViewById(R.id.btnregistrarse);

        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        setcredentiasexist();

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                String rut = txtrut.getText().toString();
                String contrasena = txtpass.getText().toString();

                Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://proyectotesis.ddns.net/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                tesisAPI tesisAPI = retrofit.create(com.example.practicadiseo.tesisAPI.class);

                Call<List<Usuario>> call = tesisAPI.getUsuario(rut,contrasena);
                call.enqueue(new Callback<List<Usuario>>() {
                    @Override
                    public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {

                        if(!response.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "error :"+response.code(), Toast.LENGTH_LONG).show();
                        }

                        List<Usuario> usuarios = response.body();

                        for (Usuario usuario:usuarios){

                            String usuarioconectado = usuario.getRut().toString();
                            String usuarioconectadopass = usuario.getContrasena().toString();

                            if(usuarioconectado == txtrut.getText().toString() && usuarioconectadopass == txtpass.getText().toString()){

                                Intent intent = new Intent(login2Activity.this, menuActivity.class);
                                startActivity(intent);

                            }


                        }

                    }

                    @Override
                    public void onFailure(Call<List<Usuario>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "error :"+t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });




            }

        });

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(login2Activity.this, register2Activity.class);
                startActivity(intent);
            }

        });





    }



   // String url = "http://proyectotesis.ddns.net/api/UsuarioAPI?id="+txtrut+"&pass="+txtpass+"";

    //metodo para enviar los datos y realizar la accion o acciones necesarias
  /*  private void enviarRequest() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //rescatas el json desde la web api
                            JSONObject jsonObject = new JSONObject(response);
                            // rescatas el valor del objeto
                            Usuario usuario = jsonObject.getJSONObject();
                            //comparas el string rescatado del objetojson y lo comparas con un ok
                            if (valor.equals("OK")) {
                                //conversion de datos a String
                                String usu = txtrut.getText().toString();
                                String contra = txtpass.getText().toString();
                                Intent intent = new Intent(login2Activity.this, menuActivity.class);
                                //guardar los datos de la session en el sp
                                saveOnPreferences(usu, contra);
                                startActivity(intent);
                            } else {
                                dp = new SweetAlertDialog(login2Activity.this, SweetAlertDialog.ERROR_TYPE);
                                dp.setTitleText("Oops...");
                                dp.setContentText("El Rut / Contraseña son incorrectos!");
                                dp.show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getMessage();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Rut", rut);
                params.put("Contrasena", Contrasena);
                return params;
            }
        };
        AppSingleton.getInstance(login2Activity.this).addToRequestQue(stringRequest);
    }

*/

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
            txtrut.setText(rut);
            txtpass.setText(contraseña);
        }
    }

    private String getuserrutprefs() {

        return prefs.getString("rut", "");
    }

    private String getusercontraseñaprefs() {

        return prefs.getString("Contraseña", "");
    }




}
