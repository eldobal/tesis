package com.example.practicadiseo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class passperfilFragment extends Fragment {
    SharedPreferences prefs;
    SweetAlertDialog dp;
    private String rutperfil ="",contrasenaperfil="",contraseñaactualcomparar="";
    private EditText contraseña1, contraseña2,contraseñaactual;
    private Button btncambiopass;
    Usuario usuario = new Usuario();
    public passperfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_passperfil, container, false);
        contraseñaactual = (EditText) v.findViewById(R.id.passactual);
        contraseña1 = (EditText) v.findViewById(R.id.cambiocontraseña);
        contraseña2 = (EditText) v.findViewById(R.id.cambiocontraseña2);
        btncambiopass = (Button) v.findViewById(R.id.btncambiocontrasena);
        Usuario usuario = new Usuario();
        prefs = this.getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        setcredentiasexist();


        cargardatosperfil();

        btncambiopass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               String contraseñaactual1 =contraseñaactual.getText().toString();
                //String  usuariocontraseña = usuario.getContrasena().toString();
                String contraseñanueva =contraseña1.getText().toString();
                String contraseñanueva2 =contraseña2.getText().toString();

                if(contraseñaactual1.equals(contraseñaactualcomparar) && (contraseñanueva.equals(contraseñanueva2))){

                    actualizarperfil();

                }
            }
        });

        return v;
    }




    private void cargardatosperfil() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://proyectotesis.ddns.net/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            tesisAPI tesisAPI = retrofit.create(com.example.practicadiseo.tesisAPI.class);



            //metodo para llamar a la funcion que queramos
            //llamar a la funcion de get usuario la cual se le envia los datos (rut y contraseña )
            Call<Usuario> call = tesisAPI.getUsuario(rutperfil,contrasenaperfil);
            call.enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse( Call<Usuario>call, Response<Usuario> response) {

                    //si esta malo se ejecuta este trozo
                    if(!response.isSuccessful()){
                        Toast.makeText(getContext(), "error :"+response.code(), Toast.LENGTH_LONG).show();
                    }
                    //de lo contrario se ejecuta esta parte
                    else {
                        //respuesta del request
                        Usuario usuarios = response.body();

                        contraseñaactualcomparar = usuarios.getContrasena().toString();

                    }
                }

                //si falla el request a la pagina mostrara este error
                @Override
                public void onFailure(Call<Usuario> call, Throwable t) {
                    Toast.makeText(getContext(), "errorc :"+t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });


    }


    private void actualizarperfil() {

            String RUT = rutperfil;
            String Contrasena = contraseña2.getText().toString();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://proyectotesis.ddns.net/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            tesisAPI tesisAPI = retrofit.create(com.example.practicadiseo.tesisAPI.class);

            try {


                //metodo para llamar a la funcion que queramos
                //llamar a la funcion de get usuario la cual se le envia los datos (rut y contraseña )
                Call<Usuario> call = tesisAPI.UsuarioPass(RUT, Contrasena);
                call.enqueue(new Callback<Usuario>() {
                    @Override
                    public void onResponse(Call<Usuario> call, Response<Usuario> response) {

                        //si esta malo se ejecuta este trozo
                        if (!response.isSuccessful()) {
                            Toast.makeText(getContext(), "error :" + response.code(), Toast.LENGTH_LONG).show();
                        }
                        //de lo contrario se ejecuta esta parte
                        else {
                            //respuesta del request
                            Usuario usuarios = response.body();

                            Intent intent = new Intent(getActivity(), menuActivity.class);
                            startActivity(intent);

                        }
                    }

                    //si falla el request a la pagina mostrara este error
                    @Override
                    public void onFailure(Call<Usuario> call, Throwable t) {
                        Toast.makeText(getContext(), "errorb :" + t.getMessage(), Toast.LENGTH_LONG).show();
                    }

                });
            }catch (Exception e){

            }

    }














    private void setcredentiasexist() {
        String rutq = getuserrutprefs();
        String contrasena = getusercontraseñaprefs();
        if (!TextUtils.isEmpty(rutq)&& (!TextUtils.isEmpty(contrasena)) ) {
            rutperfil=rutq.toString();
            contrasenaperfil=contrasena.toString();
        }
    }

    private String getuserrutprefs() {
        return prefs.getString("Rut", "");
    }

    private String getusercontraseñaprefs() {
        return prefs.getString("ContraseNa", "");
    }
}
