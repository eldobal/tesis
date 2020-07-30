package com.example.practicadiseo.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.practicadiseo.R;
import com.example.practicadiseo.activitys.menuActivity;
import com.example.practicadiseo.clases.Adaptadortrabajadores;
import com.example.practicadiseo.clases.UsuarioTrabajador;
import com.example.practicadiseo.interfaces.tesisAPI;
import com.google.android.material.snackbar.Snackbar;

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
    private TextView txtnotfound;
    private SharedPreferences prefs;
    LottieAnimationView preloaderlista,notfound;
    int idciudad =0,numeroultimo=0,filtro=0;
    final static String rutaservidor= "http://proyectotesis.ddns.net";
    SwipeRefreshLayout refreshLayouttrabajadores;
    ArrayList<UsuarioTrabajador> listatrabajadoresporrubo = new ArrayList<UsuarioTrabajador>();
    Spinner spinnerordenar ;
    String rutusuario="",contrasena="";
    NetworkInfo activeNetwork;
    ConnectivityManager cm ;

    public listabuscarrubroFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_listabuscarrubro, container, false);
        prefs = this.getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        preloaderlista =(LottieAnimationView) v.findViewById(R.id.preloaderlistatrabajador);
        notfound=(LottieAnimationView) v.findViewById(R.id.notfoundtrabajador);
        txtnotfound =(TextView) v.findViewById(R.id.txtnotfound);
        txtnotfound.setText("");
        spinnerordenar =(Spinner) v.findViewById(R.id.spinnerordenar);
        notfound.setVisibility(View.INVISIBLE);
        //se comprueba y trae el id de la ciudad del cliente
        setcredentiasexist();
        setcredentiasexistusuario();
        lista = (ListView) v.findViewById(R.id.listadoperfilestrabajadores);
        //instanciacion del refresh para la lista de los trabajadores
        refreshLayouttrabajadores = v.findViewById(R.id.refreshtrabajadores);
        final View vista = inflater.inflate(R.layout.elementoperfiltrabajador, null);

        String[] datos = new String[] {"Por Defecto","1 Estrella", "2 Estrellas", "3 Estrellas", "4 Estrellas", "5 Estrellas"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, datos);

        spinnerordenar.setAdapter(adapter);

        Bundle datosRecuperados = getArguments();
        if (datosRecuperados == null) {
            // No hay datos, manejar excepción
            updateDetail();
        }
        int idrubro = datosRecuperados.getInt("idRubro");

        if(!rutusuario.isEmpty() || !contrasena.isEmpty() ){

            cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null) {
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    //carga de los trabajdores por el rubro y por la ciudad en la cual se encuentra el usuario
                    cargartrabajadores(idrubro, idciudad);

                } else {
                    Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                            "No se ha encontrado una coneccion a Internet.", Snackbar.LENGTH_LONG);
                    snackBar.show();
                }
            }

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
                            cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                            activeNetwork = cm.getActiveNetworkInfo();
                            if (activeNetwork != null) {
                                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                                    //carga de los trabajdores por el rubro y por la ciudad en la cual se encuentra el usuario
                                    cargartrabajadores(idrubro, idciudad);
                                } else {
                                    //manejar alert
                                    Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                                            "No se ha encontrado una coneccion a Internet.", Snackbar.LENGTH_LONG);
                                    snackBar.show();
                                }
                            }
                        }
                    }.start();
                }
            });

            spinnerordenar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch(position) {
                        case 0:
                            filtro=0;
                            ordenarlista(0);
                            break;
                        case 1:
                            filtro=1;
                            ordenarlista(1);
                            break;
                        case 2:
                            filtro=2;
                            ordenarlista(2);
                            break;
                        case 3:
                            filtro=3;
                            ordenarlista(3);
                            break;
                        case 4:
                            filtro=4;
                            ordenarlista(4);
                            break;
                        case 5:
                            filtro=5;
                            ordenarlista(5);
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });



        }else{updateDetail();}

        return  v;
    }
    //metodo que filtra la lista de trabajadores por estrellas
    private void ordenarlista(int orden) {
        ArrayList<UsuarioTrabajador> listaordenada = new ArrayList<UsuarioTrabajador>();
        if(orden ==0){
            Adaptadortrabajadores ad = new Adaptadortrabajadores(getContext(), listatrabajadoresporrubo);
            lista.setAdapter(ad);
        }
        if(orden ==1){
            listaordenada.clear();
            for (int i = 0; i <listatrabajadoresporrubo.size() ; i++) {

                if(listatrabajadoresporrubo.get(i).getCalificacion().equals("1")){
                    listaordenada.add(listatrabajadoresporrubo.get(i));
                }
            }
            if(listaordenada.size() != 0) {
                Adaptadortrabajadores ad= new Adaptadortrabajadores(getContext(), listaordenada);
                ad.refresh(listaordenada);
                lista.setAdapter(ad);
            }else{
                Adaptadortrabajadores ad= new Adaptadortrabajadores(getContext(), listatrabajadoresporrubo);
                lista.setAdapter(ad);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();
                View viewsync = inflater.inflate(R.layout.alertdialogfiltroestrellas,null);
                builder.setView(viewsync);
                AlertDialog dialog7 = builder.create();
                dialog7.setCancelable(false);
                dialog7.show();
                dialog7.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView texto = (TextView) viewsync.findViewById(R.id.txtalertnotificacion);
                texto.setText("No se Han encontrado trabajadores con esta cantidad de estrellas. se mostrarà la lista por defecto");
                Button btncerrar =(Button) viewsync.findViewById(R.id.btnalertperfilexito);

                btncerrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog7.dismiss();
                    }
                });
            }
        }
        if(orden ==2){
            listaordenada.clear();
            for (int i = 0; i <listatrabajadoresporrubo.size() ; i++) {
                if(listatrabajadoresporrubo.get(i).getCalificacion().equals("2")){
                    listaordenada.add(listatrabajadoresporrubo.get(i));
                }
            }
            if(listaordenada.size() != 0) {
                Adaptadortrabajadores ad= new Adaptadortrabajadores(getContext(), listaordenada);
                ad.refresh(listaordenada);
                lista.setAdapter(ad);
            }else{
                Adaptadortrabajadores ad= new Adaptadortrabajadores(getContext(), listatrabajadoresporrubo);
                lista.setAdapter(ad);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();
                View viewsync = inflater.inflate(R.layout.alertdialogfiltroestrellas,null);
                builder.setView(viewsync);
                AlertDialog dialog6 = builder.create();
                dialog6.setCancelable(false);
                dialog6.show();
                dialog6.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView texto = (TextView) viewsync.findViewById(R.id.txtalertnotificacion);
                texto.setText("No se Han encontrado trabajadores con esta cantidad de estrellas. se mostrarà la lista por defecto");
                Button btncerrar =(Button) viewsync.findViewById(R.id.btnalertperfilexito);

                btncerrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog6.dismiss();
                    }
                });
            }
        }
        if(orden ==3){
            listaordenada.clear();
            for (int i = 0; i <listatrabajadoresporrubo.size() ; i++) {
                if(listatrabajadoresporrubo.get(i).getCalificacion().equals("3")){
                    listaordenada.add(listatrabajadoresporrubo.get(i));
                }
            }
            if(listaordenada.size() != 0) {
                Adaptadortrabajadores ad= new Adaptadortrabajadores(getContext(), listaordenada);
                ad.refresh(listaordenada);
                lista.setAdapter(ad);
            }else{
                Adaptadortrabajadores ad= new Adaptadortrabajadores(getContext(), listatrabajadoresporrubo);
                lista.setAdapter(ad);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();
                View viewsync = inflater.inflate(R.layout.alertdialogfiltroestrellas,null);
                builder.setView(viewsync);
                AlertDialog dialog5 = builder.create();
                dialog5.show();
                dialog5.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView texto = (TextView) viewsync.findViewById(R.id.txtalertnotificacion);
                texto.setText("No se Han encontrado trabajadores con esta cantidad de estrellas. se mostrarà la lista por defecto");
                Button btncerrar =(Button) viewsync.findViewById(R.id.btnalertperfilexito);

                btncerrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog5.dismiss();
                    }
                });
            }
        }
        if(orden ==4){
            listaordenada.clear();
            for (int i = 0; i <listatrabajadoresporrubo.size() ; i++) {
                if(listatrabajadoresporrubo.get(i).getCalificacion().equals("4")){
                    listaordenada.add(listatrabajadoresporrubo.get(i));
                }
            }
            if(listaordenada.size() != 0) {
                Adaptadortrabajadores ad= new Adaptadortrabajadores(getContext(), listaordenada);
                ad.refresh(listaordenada);
                lista.setAdapter(ad);
            }else{
                Adaptadortrabajadores ad= new Adaptadortrabajadores(getContext(), listatrabajadoresporrubo);
                lista.setAdapter(ad);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();
                View viewsync = inflater.inflate(R.layout.alertdialogfiltroestrellas,null);
                builder.setView(viewsync);
                AlertDialog dialog4 = builder.create();
                dialog4.show();
                dialog4.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView texto = (TextView) viewsync.findViewById(R.id.txtalertnotificacion);
                texto.setText("No se Han encontrado trabajadores con esta cantidad de estrellas. se mostrarà la lista por defecto");
                Button btncerrar =(Button) viewsync.findViewById(R.id.btnalertperfilexito);

                btncerrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog4.dismiss();
                    }
                });
            }

        }
        if(orden ==5){
            listaordenada.clear();
            for (int i = 0; i <listatrabajadoresporrubo.size() ; i++) {
                if(listatrabajadoresporrubo.get(i).getCalificacion().equals("5")){
                    listaordenada.add(listatrabajadoresporrubo.get(i));
                }
            }

            if(listaordenada.size() != 0) {
                Adaptadortrabajadores ad = new Adaptadortrabajadores(getContext(), listaordenada);
                ad.refresh(listaordenada);
                lista.setAdapter(ad);
            }else{
                Adaptadortrabajadores ad = new Adaptadortrabajadores(getContext(), listatrabajadoresporrubo);
                lista.setAdapter(ad);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();
                View viewsync = inflater.inflate(R.layout.alertdialogfiltroestrellas,null);
                builder.setView(viewsync);
                AlertDialog dialog3 = builder.create();
                dialog3.show();
                dialog3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView texto = (TextView) viewsync.findViewById(R.id.txtalertnotificacion);
                texto.setText("No se Han encontrado trabajadores con esta cantidad de estrellas. se mostrarà la lista por defecto");
                Button btncerrar =(Button) viewsync.findViewById(R.id.btnalertperfilexito);

                btncerrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog3.dismiss();
                    }
                });
            }
        }


    }

    private void cargartrabajadores(int idrubro, int idciudad) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://proyectotesis.ddns.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        tesisAPI tesisAPI = retrofit.create(com.example.practicadiseo.interfaces.tesisAPI.class);
        Call<List<UsuarioTrabajador>> call = tesisAPI.getRubroTrabajador(idrubro,idciudad,rutusuario,contrasena);
        call.enqueue(new Callback<List<UsuarioTrabajador>>() {
            @Override
            public void onResponse(Call<List<UsuarioTrabajador>> call, Response<List<UsuarioTrabajador>> response) {
                if (!response.isSuccessful()) {
                    //alert de que la respuesta fue incorrecta
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    LayoutInflater inflater = getLayoutInflater();
                    View viewsync = inflater.inflate(R.layout.alerdialogerrorresponce,null);
                    builder.setView(viewsync);
                    AlertDialog dialog2 = builder.create();
                    dialog2.setCancelable(false);
                    dialog2.show();
                    dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    TextView texto = (TextView) viewsync.findViewById(R.id.txtalertnotificacion);
                    texto.setText("Ha ocurrido un error con la respuesta al traer la lista de solicitudes. intente en un momento nuevamente.");
                    Button btncerrar =(Button) viewsync.findViewById(R.id.btnalertperfilexito);

                    btncerrar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                        }
                    });

                 //   Toast.makeText(getContext(), "error :" + response.code(), Toast.LENGTH_LONG).show();
                } else {
                    List<UsuarioTrabajador> trabajadores = response.body();
                    listatrabajadoresporrubo.clear();
                    for (UsuarioTrabajador trabajador : trabajadores) {
                        UsuarioTrabajador trabajador1 = new UsuarioTrabajador();
                        //se setean los valores del trabajador
                        trabajador1.setRUT(trabajador.getRUT().toString());
                        trabajador1.setNombre(trabajador.getNombre()+" "+trabajador.getApellido());
                        trabajador1.setCalificacion(trabajador.getCalificacion());
                        trabajador1.setCiudad(trabajador.getCiudad());
                        trabajador1.setIdRubro(trabajador.getIdRubro());
                        //declaracion de la ruta de la imagen del trabajador
                        trabajador1.setFoto(rutaservidor+trabajador.getFoto());
                        //llamada hacia getususario para instanciar el usuario
                        listatrabajadoresporrubo.add(trabajador1);
                    }
                    if (listatrabajadoresporrubo.size() > 0) {

                        txtnotfound.setText("");
                        notfound.setVisibility(View.INVISIBLE);
                        notfound.pauseAnimation();
                        preloaderlista.setVisibility(View.INVISIBLE);
                        preloaderlista.pauseAnimation();
                        ordenarlista(filtro);
                        //Adaptadortrabajadores ad = new Adaptadortrabajadores(getContext(), listatrabajadoresporrubo);
                        //lista.setAdapter(ad);

                        spinnerordenar.setVisibility(View.VISIBLE);

                    } else if(listatrabajadoresporrubo.size() == 0){
                        preloaderlista.setVisibility(View.INVISIBLE);
                        preloaderlista.pauseAnimation();
                        //si no encuentran usuario enviar al fragment con anicmacion de no encontrado
                        notfound.setVisibility(View.VISIBLE);
                        notfound.playAnimation();
                        notfound.loop(true);
                        txtnotfound.setText("No Se Han Encontrado Trabajadores para este Rubro");
                    }
                    refreshLayouttrabajadores.setRefreshing(false);
                }
            }
            @Override
            public void onFailure(Call<List<UsuarioTrabajador>> call, Throwable t) {
                txtnotfound.setText("No Se Han Encontrado Trabajadores para este Rubro");
                /*
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();
                View viewsync = inflater.inflate(R.layout.alerdialogerrorservidor,null);
                builder.setView(viewsync);
                AlertDialog dialog = builder.create();
                dialog.setCancelable(false);
                dialog.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView texto = (TextView) viewsync.findViewById(R.id.txterrorservidor);
                texto.setText("Ha ocurrido un error con la coneccion del servidor, Estamos trabajando para solucionarlo.");
                Button btncerrar =(Button) viewsync.findViewById(R.id.btncerraralert);

                btncerrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                 */
                preloaderlista.setVisibility(View.INVISIBLE);
                notfound.setVisibility(View.VISIBLE);
                notfound.playAnimation();
                notfound.loop(true);
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

    //metodo para traer el rut del usuario hacia la variable local
    private void setcredentiasexistusuario() {
        String rut = getuserrutprefs();
        String contraseña = getusercontraseñaprefs();
        if (!TextUtils.isEmpty(rut) && !TextUtils.isEmpty(contraseña)) {
            rutusuario=rut.toString();
            contrasena=contraseña.toString();
        }
    }

}
