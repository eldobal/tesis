package com.example.practicadiseo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.practicadiseo.clases.Adaptadornotificaciones;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class listanotificacionesFragment extends Fragment {

    ListView listanotificaciones;
    NetworkInfo NetworkInfo;
    SwipeRefreshLayout refreshnotificaciones;
    SharedPreferences prefs;
    ArrayList<Notificacion> arraylistanotificaciones= new ArrayList<Notificacion>();;
    Adaptadornotificaciones ads;

    private String rutusuario;



    public listanotificacionesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //lista de notificaciones en un array para recibirlas con el get arguments
        arraylistanotificaciones = (ArrayList<Notificacion>) getArguments().getSerializable("arraynotificaciones");

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo = connectivityManager.getActiveNetworkInfo();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_listanotificaciones, container, false);
        //prefs que contienen datos del usuario
        prefs = this.getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        setcredentiasexist();
        listanotificaciones = (ListView) v.findViewById(R.id.listanotificaciones);
        refreshnotificaciones =(SwipeRefreshLayout) v.findViewById(R.id.refreshnotificaciones);

        if (rutusuario.isEmpty()){
            //enviar al usuario hacia alguna pantalla de home y mostrar el error en forma de mensaje
            Intent intent = new Intent(getContext(), login2Activity.class);
            startActivity(intent);
        }else {
            //if (Solicitudes.size() > 0) {
            final View vista = inflater.inflate(R.layout.elementonotificacion, null);
            //se instancia el adaptadador en el cual se instanciara la lista de trbajadres para setearlas en el apdaptador
            if (arraylistanotificaciones.size() != 0) {
                ads = new Adaptadornotificaciones(getContext(), arraylistanotificaciones);
                //se setea el adaptador a la lista del fragments
                listanotificaciones.setAdapter(ads);
            }

        }



        refreshnotificaciones.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new CountDownTimer(1500,1000){
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }
                    @Override
                    public void onFinish() {
                        reiniciarfragmentnotificaciones(rutusuario);
                    }
                }.start();
            }
        });


        final Handler handler = new Handler();
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            //Ejecuta tu AsyncTask!
                          reiniciarfragmentnotificaciones(rutusuario);
                        } catch (Exception e) {
                            Log.e("error", e.getMessage());
                        }
                    }
                });
            }
        };
        timer.schedule(task, 0, 60000);  //ejecutar en intervalo definido por el programador




        return v;
    }



    private void reiniciarfragmentnotificaciones(String rutusuario) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://proyectotesis.ddns.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        tesisAPI tesisAPI = retrofit.create(com.example.practicadiseo.tesisAPI.class);
        Call<List<Notificacion>> call = tesisAPI.getNotificacion(rutusuario);
        call.enqueue(new Callback<List<Notificacion>>() {
            @Override
            public void onResponse(Call<List<Notificacion>> call, Response<List<Notificacion>> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), "error :" + response.code(), Toast.LENGTH_LONG).show();
                } else {
                    arraylistanotificaciones.clear();
                    List<Notificacion> notificaciones = response.body();
                    for (Notificacion notificacion : notificaciones) {
                        Notificacion notificacion1 = new Notificacion();
                        //se setean los valores del trabajador
                        notificacion1.setId(notificacion.getId());
                        notificacion1.setIdSolicitud(notificacion.getIdSolicitud());
                        notificacion1.setMensaje(notificacion.getMensaje());
                        notificacion1.setRUT(notificacion.getRUT());
                        //se guarda la lista con las notificaciones del usuario conectado
                        arraylistanotificaciones.add(notificacion1);
                    }

                    if (arraylistanotificaciones.size() != 0) {
                        //se instancia la recarga de los items que se encuentan en la lista de activas / pendientes
                        ads.refresh(arraylistanotificaciones);
                    }
                }
                refreshnotificaciones.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<List<Notificacion>> call, Throwable t) {
                Toast.makeText(getActivity(), "error :" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });



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
