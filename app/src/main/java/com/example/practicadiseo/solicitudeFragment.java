package com.example.practicadiseo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.text.method.DateTimeKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    private ListView lista,listaactivas;
    private ImageButton btnVolver;
    private SharedPreferences prefs;
    private String rutusuario;
    ArrayList<Solicitud> listasolicitudesterminadas;
    ArrayList<Solicitud> listasolicitudactivas;
    ArrayList<Solicitud> Solicitudescomparar;
    ArrayList<Solicitud> Solicitudes = new ArrayList<Solicitud>();
    Boolean recargado=false;;

    public solicitudeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lista = (ListView) getActivity().findViewById(R.id.listadosolicitudescliente);
        listaactivas = (ListView) getActivity().findViewById(R.id.solicitudactual);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Solicitudescomparar = new ArrayList<Solicitud>();
        listasolicitudesterminadas  = new ArrayList<Solicitud>();
        listasolicitudactivas  = new ArrayList<Solicitud>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_solicitudes, container, false);

        prefs = this.getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        //comprobacion de que el rut del usuario exista

        setcredentiasexist();
        if (rutusuario.isEmpty()){
            //enviar al usuario hacia alguna pantalla de home y mostrar el error en forma de mensaje
            Intent intent = new Intent(getContext(), login2Activity.class);
            startActivity(intent);
        }else {
            if(recargado==false){
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

                            if(Solicitudescomparar !=Solicitudes) {

                                if (Solicitudes.size() > 0) {

                                    Solicitudescomparar = Solicitudes;
                                    for (int i = 0; i < Solicitudes.size(); i++) {
                                        Solicitud soli = new Solicitud();
                                        soli = Solicitudes.get(i);
                                        if (soli.getEstado().equals("PENDIENTE")) {
                                          listasolicitudactivas.add(soli);
                                        } else {
                                            listasolicitudesterminadas.add(soli);
                                        }
                                    }
                                    final View vista = inflater.inflate(R.layout.elemento_solicitud, null);
                                    //se instancia el adaptadador en el cual se instanciara la lista de trbajadres para setearlas en el apdaptador
                                    if (listasolicitudactivas.size() != 0) {
                                        Adaptador ads = new Adaptador(getContext(), listasolicitudactivas);
                                        //se setea el adaptador a la lista del fragments
                                        listaactivas.setAdapter(ads);

                                    }
                                    if (listasolicitudesterminadas.size() != 0) {
                                        Adaptador ads = new Adaptador(getContext(), listasolicitudesterminadas);
                                        //se setea el adaptador a la lista del fragments
                                        lista.setAdapter(ads);
                                    }
                                } else if (Solicitudes.size() == 0) {
                                    dp = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
                                    dp.setTitleText("UwU");
                                    dp.setContentText("No se han encontrado Trabajadores!");
                                    dp.show();
                                }
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Solicitud>> call, Throwable t) {
                        Toast.makeText(v.getContext(), "error :" + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

                recargado =true;
            }else {
                showSelectedFragment(new solicitudeFragment());
            }

        }
        return v;
    }

    //metodo que permite elejir un fragment
    private void showSelectedFragment(Fragment fragment){
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                //permite regresar hacia atras entre los fragments
                .addToBackStack(null)
                .commit();
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