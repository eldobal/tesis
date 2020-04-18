package com.example.practicadiseo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;

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

public class login2Activity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    //codigo para utilizar la api de google
    public static final int  Signincode = 777;
    private GoogleSignInClient googleSignInClient;
    SweetAlertDialog dp;
    private SharedPreferences prefs;
    private EditText txtrut,txtpass;
    private Button btnlogin,btnregister;
    private SignInButton signInButton;

    public login2Activity() {
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        //google gso
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        //google api







        signInButton =(SignInButton) findViewById(R.id.Signbutton);
        txtrut = (EditText) findViewById(R.id.txtemail);
        txtpass = (EditText) findViewById(R.id.txtpassword);
        btnlogin = (Button) findViewById(R.id.btnlogin);
        btnregister = (Button) findViewById(R.id.btnregistrarse);
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        setcredentiasexist();


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.Signbutton:
                    singIn();
                    break;
                }

            }
        });


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    String rut = txtrut.getText().toString();
                    String contrasena = txtpass.getText().toString();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://proyectotesis.ddns.net/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    tesisAPI tesisAPI = retrofit.create(com.example.practicadiseo.tesisAPI.class);

                    //metodo para llamar a la funcion que queramos
                    Call<Usuario> call = tesisAPI.getUsuario(rut,contrasena);
                    call.enqueue(new Callback<Usuario>() {
                        @Override
                        public void onResponse(Call<Usuario> call, Response<Usuario> response) {

                            //si esta malo se ejecuta este trozo
                            if(!response.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "error :"+response.code(), Toast.LENGTH_LONG).show();
                            }
                            //de lo contrario se ejecuta esta parte
                            else {
                                //respuesta del request
                                Usuario usuarios = response.body();

                                //declaracion de variables del response
                                String usuarioconectadopass = usuarios.getContrasena().toString();
                                String usuarioconectado = usuarios.getRut().toString();

                                //if que compara los datos rescatados del response con los datos ingresados
                                if (usuarioconectado.equals(rut) && usuarioconectadopass.equals(contrasena)) {
                                    Intent intent = new Intent(login2Activity.this, menuActivity.class);
                                    startActivity(intent);
                                }
                            }
                        }

                        //si falla el request a la pagina mostrara este error
                        @Override
                        public void onFailure(Call<Usuario> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "error :"+t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }

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


    private void singIn(){
        Intent signIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signIntent,Signincode);
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


    @Override
    protected void onStart() {

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (account!=null){
            goMainScreen();
        }
        super.onStart();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == Signincode) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }



    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if(account!=null){
            // Signed in successfully, show authenticated UI.
            goMainScreen();}

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
        }
    }



    //metodo para diririr al usuario hacia una activity que queramos
    private void goMainScreen() {
        Intent intent = new Intent(this,menuActivity.class);
       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
