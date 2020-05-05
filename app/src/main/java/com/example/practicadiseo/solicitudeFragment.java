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
    private String rutusuario;
    ArrayList<Solicitud> Solicitudes = new ArrayList<Solicitud>();

    public solicitudeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_solicitudes, container, false);
        prefs = this.getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        //comprobacion de que el rut del usuario exista
        setcredentiasexist();

        if (rutusuario.isEmpty()){
            //enviar al usuario hacia alguna pantalla de home y mostrar el error en forma de mensaje
        }else {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://proyectotesis.ddns.net/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            tesisAPI tesisAPI = retrofit.create(com.example.practicadiseo.tesisAPI.class);
            Call<List<Solicitud>> call = tesisAPI.getSolicitudes(rutusuario);
            call.enqueue(new Callback<List<Solicitud>>() {
                @Override
                public void onResponse(Call<List<Solicitud>> call, Response<List<Solicitud>> response) {

                    if (!response.isSuccessful()) {
                        Toast.makeText(v.getContext(), "error :" + response.code(), Toast.LENGTH_LONG).show();
                    } else {
                        List<Solicitud> solicituds = response.body();
                        for (Solicitud solicitud : solicituds) {
                            Solicitud Solicitud1 = new Solicitud();
                            //se setean los valores del trabajador
                            Solicitud1.setIdSolicitud(solicitud.getIdSolicitud());
                            Solicitud1.setFechaS(solicitud.getFechaS());
                            Solicitud1.setNombre(solicitud.getNombre());
                            Solicitud1.setApellido(solicitud.getApellido());
                            Solicitud1.setEstado(solicitud.getEstado());
                            Solicitudes.add(Solicitud1);
                        }
                        if (Solicitudes.size() > 0) {
                            final View vista = inflater.inflate(R.layout.elemento_solicitud, null);
                            lista = (ListView) v.findViewById(R.id.listadosolicitudescliente);
                            //se instancia el adaptadador en el cual se instanciara la lista de trbajadres para setearlas en el apdaptador
                            Adaptador ads = new Adaptador(getContext(), Solicitudes);
                            //se setea el adaptador a la lista del fragments
                            lista.setAdapter(ads);
                        } else if (Solicitudes.size() == 0) {
                            dp = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
                            dp.setTitleText("UwU");
                            dp.setContentText("No se han encontrado Trabajadores!");
                            dp.show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Solicitud>> call, Throwable t) {
                    Toast.makeText(v.getContext(), "error :" + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });


        }
        return v;
    }
    //metodo para traer el rut del usuario hacia la variable local
    private void setcredentiasexist() {
        String rut = getuserrutprefs();
        if (!TextUtils.isEmpty(rut)) {
            rutusuario=rut.toString();
        }
    }

    private String getuserrutprefs() {
        return prefs.getString("Rut", "");
    }
}
