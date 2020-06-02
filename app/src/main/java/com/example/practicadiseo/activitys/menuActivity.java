package com.example.practicadiseo.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.practicadiseo.fragments.HomeFragment;
import com.example.practicadiseo.clases.Notificacion;
import com.example.practicadiseo.R;
import com.example.practicadiseo.clases.Solicitud;
import com.example.practicadiseo.fragments.listanotificacionesFragment;
import com.example.practicadiseo.fragments.mapaFragment;
import com.example.practicadiseo.fragments.perfilFragment;
import com.example.practicadiseo.fragments.settingsFragment;
import com.example.practicadiseo.fragments.solicitudeFragment;
import com.example.practicadiseo.interfaces.tesisAPI;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class menuActivity extends AppCompatActivity implements mapaFragment.OnFragmentInteractionListener {
    private GoogleSignInClient googleSignInClient;
    TextView nombre,email,id;
    ImageView fotoperfil;
    BottomNavigationView mbottomNavigationView;
    SweetAlertDialog dp;
    private SharedPreferences prefs;
    int contador=0;

    SwipeRefreshLayout refreshLayout;

    String rut="";
    ArrayList<Solicitud> listasolicitudesterminadas = new ArrayList<Solicitud>();
    ArrayList<Solicitud> listasolicitudactivas = new ArrayList<Solicitud>();
    ArrayList<Solicitud> Solicitudescomparar = new ArrayList<Solicitud>();
    ArrayList<Solicitud> Solicitudes = new ArrayList<Solicitud>();

    ArrayList<Notificacion> listanotificaciones = new ArrayList<Notificacion>();
    final static String rutaservidor= "http://proyectotesis.ddns.net";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        setcredentiasexist();
        //al momento de crear el home en el onCreate cargar con el metodo sin backtostack
        iniciarfragmentsolitudes();
        iniciarfragmentnotificaciones();
        perfilFragment perfilFragment =new perfilFragment();
        solicitudeFragment solicitudeFragment=new solicitudeFragment();
        listanotificacionesFragment listanotificacionesFragment = new listanotificacionesFragment();
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            //se instancia el gso
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            // trae el cliende de google
            googleSignInClient = GoogleSignIn.getClient(this, gso);

            //trozo de codigo para rescatar parametros de la cuenta de usuario
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();
                Toast.makeText(menuActivity.this, "Nombre"+personName+" Correo: "+personEmail+ " id:" +personId+"", Toast.LENGTH_LONG).show();
            }
            mbottomNavigationView=(BottomNavigationView) findViewById(R.id.bottomnavigation);
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();


            mbottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    //se muestra el fragment de peril
                    if(menuItem.getItemId()== R.id.menu_profile){
                        showSelectedFragment(new perfilFragment());
                    }
                    //se muestra el fragment de rubros
                    if(menuItem.getItemId()== R.id.menu_home){
                        showSelectedFragment(new HomeFragment());
                    }
                    //se muestra el fragment de la lista de solicitudes
                    if(menuItem.getItemId()==R.id.menu_solicitud){
                        //   showSelectedFragment(new solicitudeFragment());
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("arraylistaspendientes", listasolicitudactivas);
                        bundle.putSerializable("arraylistasterminadas", listasolicitudesterminadas);
                        solicitudeFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, solicitudeFragment, "solicitudtag")
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                //permite regresar hacia atras entre los fragments
                                .addToBackStack(null)
                                .commit();
                    }
                    //se muestra el fragment de configuracion y setting
                    if(menuItem.getItemId()== R.id.menu_settings){
                        showSelectedFragment(new settingsFragment());
                    }
                    if(menuItem.getItemId()== R.id.menu_notificaciones){
                        Bundle bundle2 = new Bundle();
                        bundle2.putSerializable("arraynotificaciones", listanotificaciones);
                        listanotificacionesFragment.setArguments(bundle2);
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, listanotificacionesFragment, "notificacionestag")
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                //permite regresar hacia atras entre los fragments
                                .addToBackStack(null)
                                .commit();
                    }

                    return true;
                }
            });
        }else{
            //no hay internet/coneccion manejar excepcion
            Toast.makeText(menuActivity.this, "Revise su Concexion", Toast.LENGTH_LONG).show();
        }

    }

    private void iniciarfragmentnotificaciones() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://proyectotesis.ddns.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        tesisAPI tesisAPI = retrofit.create(com.example.practicadiseo.interfaces.tesisAPI.class);
        Call<List<Notificacion>> call = tesisAPI.getNotificacion(rut);
        call.enqueue(new Callback<List<Notificacion>>() {
            @Override
            public void onResponse(Call<List<Notificacion>> call, Response<List<Notificacion>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(menuActivity.this, "error :" + response.code(), Toast.LENGTH_LONG).show();
                } else {
                    listanotificaciones.clear();
                    List<Notificacion> notificaciones = response.body();
                    for (Notificacion notificacion : notificaciones) {
                        Notificacion notificacion1 = new Notificacion();
                        //se setean los valores del trabajador
                        notificacion1.setId(notificacion.getId());
                        notificacion1.setIdSolicitud(notificacion.getIdSolicitud());
                        notificacion1.setMensaje(notificacion.getMensaje());
                        notificacion1.setRUT(notificacion.getRUT());
                        //se guarda la lista con las notificaciones del usuario conectado
                        listanotificaciones.add(notificacion1);
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Notificacion>> call, Throwable t) {
                Toast.makeText(menuActivity.this, "error :" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void iniciarfragmentsolitudes() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://proyectotesis.ddns.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        tesisAPI tesisAPI = retrofit.create(com.example.practicadiseo.interfaces.tesisAPI.class);
        Call<List<Solicitud>> call = tesisAPI.getSolicitudes(rut);
        call.enqueue(new Callback<List<Solicitud>>() {
            @Override
            public void onResponse(Call<List<Solicitud>> call, Response<List<Solicitud>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(menuActivity.this, "error :" + response.code(), Toast.LENGTH_LONG).show();
                } else {
                    listasolicitudactivas.clear();
                    listasolicitudesterminadas.clear();
                    List<Solicitud> solicituds = response.body();
                    for (Solicitud solicitud : solicituds) {
                        Solicitud Solicitud1 = new Solicitud();
                        //se setean los valores del trabajador
                        Solicitud1.setIdSolicitud(solicitud.getIdSolicitud());
                        Solicitud1.setFechaS(solicitud.getFechaS());
                        Solicitud1.setNombre(solicitud.getNombre());
                        Solicitud1.setApellido(solicitud.getApellido());
                        Solicitud1.setEstado(solicitud.getEstado());
                        Solicitud1.setFotoT(rutaservidor+solicitud.getFotoT());
                        Solicitudes.add(Solicitud1);
                    }
                    if(Solicitudescomparar !=Solicitudes) {

                        if (Solicitudes.size() > 0) {

                            Solicitudescomparar = Solicitudes;
                            for (int i = 0; i < Solicitudes.size(); i++) {
                                Solicitud soli = new Solicitud();
                                soli = Solicitudes.get(i);
                                if (soli.getEstado().equals("PENDIENTE") || soli.getEstado().equals("ATENDIENDO") || soli.getEstado().equals("CONFIRMADA")  ) {
                                    listasolicitudactivas.add(soli);
                                } else {
                                    listasolicitudesterminadas.add(soli);
                                }
                            }
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Solicitud>> call, Throwable t) {
                Toast.makeText(menuActivity.this, "error :" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


/*
    //metodo para que cuando el usuario este en la actividad principal y quiera retrocer tenga que apretar dos veces el back
    @Override
    public void onBackPressed() {

        if (contador==0){
            Toast.makeText(menuActivity.this, "Precione nuevamente para salir", Toast.LENGTH_LONG).show();
            contador++;
        }else{
            super.onBackPressed();
        }
        new CountDownTimer(3000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                contador=0;
            }
        }.start();
    }
*/

    //metodo que permite elejir un fragment
    private void showSelectedFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                //permite regresar hacia atras entre los fragments
                .addToBackStack(null)
                .commit();
    }

    //metodo que permite elejir un fragment y no volver hacia atras
    private void cargarfragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().add(R.id.container,fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                //permite regresar hacia atras entre los fragments
                .addToBackStack(null)
                .commit();
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
       String rutc = getuserrutprefs();

        if (!TextUtils.isEmpty(rutc)) {
            rut=rutc;
          //  txtpass.setText(contraseña);
        }
    }

    private String getuserrutprefs() {
        return prefs.getString("Rut", "");
    }

    private String getusercontraseñaprefs() {
        return prefs.getString("ContraseNa", "");
    }


}
