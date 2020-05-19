package com.example.practicadiseo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
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
public class listabuscarrubroFragment extends Fragment {
    SweetAlertDialog dp;
    private ListView lista;
    private SharedPreferences prefs;
    int idciudad =0,numeroultimo=0;
    final static String rutaservidor= "http://proyectotesis.ddns.net";
    SwipeRefreshLayout refreshLayouttrabajadores;
    ArrayList<UsuarioTrabajador> listatrabajadoresporrubo = new ArrayList<UsuarioTrabajador>();

    public listabuscarrubroFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_listabuscarrubro, container, false);
        prefs = this.getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        //se comprueba y trae el id de la ciudad del cliente
        setcredentiasexist();
        lista = (ListView) v.findViewById(R.id.listadoperfilestrabajadores);
        //instanciacion del refresh para la lista de los trabajadores
        refreshLayouttrabajadores = v.findViewById(R.id.refreshtrabajadores);
        final View vista = inflater.inflate(R.layout.elementoperfiltrabajador, null);

        Bundle datosRecuperados = getArguments();
        if (datosRecuperados == null) {
            // No hay datos, manejar excepción
            updateDetail();
        }
        int idrubro = datosRecuperados.getInt("idRubro");

        //carga de los trabajdores por el rubro y por la ciudad en la cual se encuentra el usuario
        cargartrabajadores(idrubro,idciudad);

        //refresh para recargar la lista de los trabajadores
        refreshLayouttrabajadores.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new CountDownTimer(1000,1000){
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }
                    @Override
                    public void onFinish() {
                        cargartrabajadores(idrubro,idciudad);
                    }
                }.start();
            }
        });


        return  v;
    }

    private void cargartrabajadores(int idrubro, int idciudad) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://proyectotesis.ddns.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        tesisAPI tesisAPI = retrofit.create(com.example.practicadiseo.tesisAPI.class);
        Call<List<UsuarioTrabajador>> call = tesisAPI.getRubroTrabajador(idrubro,idciudad);
        call.enqueue(new Callback<List<UsuarioTrabajador>>() {
            @Override
            public void onResponse(Call<List<UsuarioTrabajador>> call, Response<List<UsuarioTrabajador>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "error :" + response.code(), Toast.LENGTH_LONG).show();
                } else {
                    List<UsuarioTrabajador> trabajadores = response.body();
                    listatrabajadoresporrubo.clear();
                    for (UsuarioTrabajador trabajador : trabajadores) {
                        UsuarioTrabajador trabajador1 = new UsuarioTrabajador();
                        //se setean los valores del trabajador
                        trabajador1.setRUT(trabajador.getRUT().toString());
                        trabajador1.setNombre(trabajador.getNombre());
                        trabajador1.setCalificacion(trabajador.getCalificacion());
                        trabajador1.setCiudad(trabajador.getCiudad());
                        trabajador1.setIdRubro(trabajador.getIdRubro());
                        //declaracion de la ruta de la imagen del trabajador
                        trabajador1.setFoto(rutaservidor+trabajador.getFoto());
                        //llamada hacia getususario para instanciar el usuario
                        listatrabajadoresporrubo.add(trabajador1);
                    }
                    if (listatrabajadoresporrubo.size() > 0) {
                        Adaptadortrabajadores ad = new Adaptadortrabajadores(getContext(), listatrabajadoresporrubo);
                        lista.setAdapter(ad);
                    } else if(listatrabajadoresporrubo.size() == 0){
                        //si no encuentran usuario enviar al fragment con anicmacion de no encontrado
                        showSelectedFragment(new notfoundFragment());
                    }
                    refreshLayouttrabajadores.setRefreshing(false);
                }
            }
            @Override
            public void onFailure(Call<List<UsuarioTrabajador>> call, Throwable t) {
                showSelectedFragment(new notfoundFragment());
            }
        });

    }

    private void setcredentiasexist() {
        int ciudadid =getuseridciudadprefs();
        //string para asignar los valores del usuario si es que existe
        idciudad=ciudadid;
    }

    private void showSelectedFragment(Fragment fragment){
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment)
                .replace(R.id.container,fragment)
                //permite regresar hacia atras entre los fragments
                .addToBackStack(null)
                .commit();
    }

    public void updateDetail() {
        Intent intent = new Intent(getActivity(), menuActivity.class);
        startActivity(intent);
    }

    private String getuserrutprefs() {
        return prefs.getString("Rut", "");
    }

    private int getuseridciudadprefs() {
        return prefs.getInt("idCiudad", 0);
    }

    private String getusercontraseñaprefs() {
        return prefs.getString("ContraseNa", "");
    }


}
