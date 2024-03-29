package com.example.practicadiseo.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.practicadiseo.R;
import com.example.practicadiseo.activitys.login2Activity;
import com.example.practicadiseo.clases.Adaptadornotificaciones;
import com.example.practicadiseo.clases.GlobalInfo;
import com.example.practicadiseo.clases.Notificacion;
import com.example.practicadiseo.interfaces.tesisAPI;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.airbnb.lottie.L.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class listanotificacionesFragment extends Fragment {

    ListView listanotificaciones;
    NetworkInfo activeNetwork;
    ConnectivityManager cm ;
    SwipeRefreshLayout refreshnotificaciones;
    SharedPreferences prefs,asycprefs;
    LottieAnimationView animationnotification ;
    ArrayList<Notificacion> arraylistanotificaciones= new ArrayList<Notificacion>();;
    Adaptadornotificaciones ads ,adsnoti;
    private String rutusuario="",contrasena="";
    int azynctiempo =0;

    public listanotificacionesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) { super.onCreate(savedInstanceState);
        //lista de notificaciones en un array para recibirlas con el get arguments
        prefs = this.getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        asycprefs = this.getActivity().getSharedPreferences("asycpreferences", Context.MODE_PRIVATE);
        //prefs que contienen datos del usuario
        setcredentiasexist();
        settiempoasyncexist();

        listanotificacionesFragment test = (listanotificacionesFragment) getActivity().getSupportFragmentManager().findFragmentByTag("notificacionestag");

        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        activeNetwork = cm.getActiveNetworkInfo();
                        if (activeNetwork != null) {
                            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                                // connected to wifi or cellphone data
                                if (test != null && test.isVisible()) {
                                    //Ejecuta tu AsyncTask!
                                    // reiniciarfragment(rutusuario, contrasena);
                                    reiniciarfragmentnotificacionesASYNC(rutusuario);
                                }
                            } else {
                                //manejar excepccion
                                Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                                        "No se ha encontrado una coneccion a Internet.", Snackbar.LENGTH_LONG);
                                snackBar.show();
                            }
                        }
                    }
                });
            }
        };
        timer.schedule(task, 0, azynctiempo);  //ejecutar en intervalo definido por el programador
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_listanotificaciones, container, false);
        listanotificaciones = (ListView) v.findViewById(R.id.listanotificaciones);
        animationnotification = (LottieAnimationView) v.findViewById(R.id.animationotification);

        if (rutusuario.isEmpty() || contrasena.isEmpty()){
            //enviar al usuario hacia alguna pantalla de home y mostrar el error en forma de mensaje
            Intent intent = new Intent(getContext(), login2Activity.class);
            startActivity(intent);
        }else{
            //if (Solicitudes.size() > 0) {
            final View vista = inflater.inflate(R.layout.elementonotificacion, null);
            cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null) {
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    reiniciarfragmentnotificacionesASYNC(rutusuario);
                } else {
                    //dialog de aviso
                    Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                            "No se ha encontrado una coneccion a Internet.", Snackbar.LENGTH_LONG);
                    snackBar.show();
                }
            }


        }

        return v;
    }

    private void reiniciarfragmentnotificacionesASYNC(String rutusuario) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GlobalInfo.Rutaservidor)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        tesisAPI tesisAPI = retrofit.create(com.example.practicadiseo.interfaces.tesisAPI.class);
        Call<List<Notificacion>> call = tesisAPI.getNotificacion(rutusuario,contrasena);
        call.enqueue(new Callback<List<Notificacion>>() {
            @Override
            public void onResponse(Call<List<Notificacion>> call, Response<List<Notificacion>> response) {
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
                    texto.setText("Ha ocurrido un error con la respuesta al traer las notificaciones intente en un momento nuevamente.");
                    Button btncerrar =(Button) viewsync.findViewById(R.id.btnalertperfilexito);


                    btncerrar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                        }
                    });


                  //  Toast.makeText(getActivity(), "error :" + response.code(), Toast.LENGTH_LONG).show();
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
                        adsnoti = new Adaptadornotificaciones(getContext(), arraylistanotificaciones);
                        listanotificaciones.setAdapter(adsnoti);
                        // adsnoti.refresh(arraylistanotificaciones);
                        animationnotification.setVisibility(View.INVISIBLE);
                        animationnotification.pauseAnimation();
                        //se instancia la recarga de los items que se encuentan en la lista de activas / pendientes
                    }else {
                        animationnotification.setVisibility(View.VISIBLE);
                        animationnotification.playAnimation();
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Notificacion>> call, Throwable t) {
                //alert que hay un error con el servidor
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

              //  Toast.makeText(getActivity(), "error :" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    //metodo para traer el rut del usuario hacia la variable local
    private void setcredentiasexist() {
        String rut = getuserrutprefs();
        String contraseña = getusercontraseñaprefs();
        if (!TextUtils.isEmpty(rut) && !TextUtils.isEmpty(contraseña)) {
            rutusuario=rut.toString();
            contrasena=contraseña.toString();
        }
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

    private void settiempoasyncexist() {
        int tiempoasync = gettiempoasync();
        if (tiempoasync!=0) {
            azynctiempo=tiempoasync;
        }
    }

    private int gettiempoasync() {
        return asycprefs.getInt("tiempo", 0);
    }



}
