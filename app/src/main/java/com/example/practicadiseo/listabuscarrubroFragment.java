package com.example.practicadiseo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
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
    int idciudad =0;

    ArrayList<UsuarioTrabajador> listatrabajadoresporrubo = new ArrayList<UsuarioTrabajador>();
    ArrayList<UsuarioTrabajador> listatrabajadores = new ArrayList<UsuarioTrabajador>();


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

        Bundle datosRecuperados = getArguments();
        if (datosRecuperados == null) {
            // No hay datos, manejar excepción
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://proyectotesis.ddns.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        tesisAPI tesisAPI = retrofit.create(com.example.practicadiseo.tesisAPI.class);
        //declaracion estatica del tipo de usuario el cual debera ser siempre trabajador
        int idrubro = datosRecuperados.getInt("idRubro");
        Call<List<UsuarioTrabajador>> call = tesisAPI.getRubroTrabajador(idrubro,idciudad);
        //lamada usuarios por rubro
        call.enqueue(new Callback<List<UsuarioTrabajador>>() {
            @Override
            public void onResponse(Call<List<UsuarioTrabajador>> call, Response<List<UsuarioTrabajador>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(v.getContext(), "error :" + response.code(), Toast.LENGTH_LONG).show();
                } else {
                    List<UsuarioTrabajador> trabajadores = response.body();

                    for (UsuarioTrabajador trabajador : trabajadores) {
                        UsuarioTrabajador trabajador1 = new UsuarioTrabajador();
                        //se setean los valores del trabajador
                        trabajador1.setRUT(trabajador.getRUT().toString());
                        trabajador1.setNombre(trabajador.getNombre());
                        trabajador1.setCalificacion(trabajador.getCalificacion());
                        trabajador1.setCiudad(trabajador.getCiudad());
                        trabajador1.setIdRubro(trabajador.getIdRubro());
                       //llamada hacia getususario para instanciar el usuario
                        listatrabajadoresporrubo.add(trabajador1);
                    }
                    if (listatrabajadoresporrubo.size() > 0) {
                        final View vista = inflater.inflate(R.layout.elementoperfiltrabajador, null);
                        lista = (ListView) v.findViewById(R.id.listadoperfilestrabajadores);
                        Adaptadortrabajadores ad = new Adaptadortrabajadores(getContext(), listatrabajadoresporrubo);
                        lista.setAdapter(ad);
                    } else if(listatrabajadoresporrubo.size() == 0){
                        dp = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
                        dp.setTitleText("UwU");
                        dp.setContentText("No se han encontrado solicitudes pendientes!");
                        dp.show();
                    }
                }
            }
            @Override
            public void onFailure(Call<List<UsuarioTrabajador>> call, Throwable t) {
                Toast.makeText(v.getContext(), "error :"+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        return  v;
    }

    private void setcredentiasexist() {
        int ciudadid =getuseridciudadprefs();
        //string para asignar los valores del usuario si es que existe
        idciudad=ciudadid;
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
