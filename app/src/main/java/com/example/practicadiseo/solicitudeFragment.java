package com.example.practicadiseo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.text.method.DateTimeKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class solicitudeFragment extends Fragment {

    SweetAlertDialog dp;
    private static LayoutInflater inflater = null;
    private TextView tvNoRegistros;
    private ListView lista;
    private ImageButton btnVolver;
    private SharedPreferences prefs;
    private TextView rutusuario;


    ArrayList<Solicitud> solicitudes = new ArrayList<Solicitud>();
    public solicitudeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_solicitudes, container, false);
        SharedPreferences prefs = this.getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        setcredentiasexist();

/*

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://proyectotesis.ddns.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        tesisAPI tesisAPI = retrofit.create(com.example.practicadiseo.tesisAPI.class);

        Call<List<Solicitud>> call = tesisAPI.getSolicitudes(rutusuario.getText().toString());

        call.enqueue(new Callback<List<Solicitud>>() {
            @Override
            public void onResponse(Call<List<Solicitud>> call, Response<List<Solicitud>> response) {

                if(!response.isSuccessful()){
                    Toast.makeText(v.getContext(), "error :"+response.code(), Toast.LENGTH_LONG).show();
                }

                List<Solicitud> usuarios = response.body();

                for (Solicitud usuario:usuarios){

                    String usuarioconectado = usuario.getRut().toString();
                    String usuarioconectadopass = usuario.getContrasena().toString();

                    if(usuarioconectado == txtrut.getText().toString() && usuarioconectadopass == txtpass.getText().toString()){

                        Intent intent = new Intent(v.getContext(), menuActivity.class);
                        startActivity(intent);

                    }


                }

            }

            @Override
            public void onFailure(Call<List<Solicitud>> call, Throwable t) {
                Toast.makeText(v.getContext(), "error :"+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


*/
        return v;
    }


    private void setcredentiasexist() {
        String rut = getuserrutprefs();
        if (!TextUtils.isEmpty(rut)) {
            rutusuario.setText(rut);
        }
    }

    private String getuserrutprefs() {
        return prefs.getString("rut", "");
    }
}
