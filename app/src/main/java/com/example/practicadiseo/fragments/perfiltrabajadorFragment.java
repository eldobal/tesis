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
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.practicadiseo.R;
import com.example.practicadiseo.activitys.menuActivity;
import com.example.practicadiseo.activitys.register2Activity;
import com.example.practicadiseo.clases.UsuarioTrabajador;
import com.example.practicadiseo.fragments.crearsolicitudFragment;
import com.example.practicadiseo.interfaces.tesisAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class perfiltrabajadorFragment extends Fragment {
    private TextView rut,nombre,apellido,correo,telefono,ciudad,estado,calificacion;
    private String nombretrabajor="",estadotrabador="",calificaciontrabajador="",ruttrabajador="";
    private int idrubro=0;
    LottieAnimationView loadingdots2 ;
    private Button btncrearsolicitud;
    private ImageView foto;
    final static String rutaservidor= "http://proyectotesis.ddns.net";
    String urlfoto="";
    NetworkInfo networkInfo;
    SharedPreferences prefsmaps,prefs;
    UsuarioTrabajador trabajador =new UsuarioTrabajador();

    private String rutusuario="",contrasena="";
    public perfiltrabajadorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_perfiltrabajador, container, false);
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        Bundle args = getArguments();
        prefs = this.getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        setcredentiasexist();
        if (args == null) {
            // No hay datos, manejar excepción
        }else{
             ruttrabajador= args.getString("ruttrabajador");
             idrubro = args.getInt("idrubro");
        }

        rut= (TextView) v.findViewById(R.id.txtrutperfiltrabajador);
        nombre = (TextView) v.findViewById(R.id.txtnombreperfiltrabajador);
        telefono = (TextView) v.findViewById(R.id.txttelefonoperfiltrabajador);
        ciudad = (TextView) v.findViewById(R.id.txtciudadperfiltrabajador);
        estado = (TextView) v.findViewById(R.id.txtestadoperfiltrabajador);
        calificacion = (TextView) v.findViewById(R.id.txtcalificacionperfiltrabajador);
        btncrearsolicitud=(Button) v.findViewById(R.id.botoncrearsolicitud);
        foto = (ImageView) v.findViewById(R.id.imgperfiltrabajadordetalle);
        loadingdots2 =(LottieAnimationView) v.findViewById(R.id.loadindots2);


        if(ruttrabajador.isEmpty()||rutusuario.isEmpty() ||contrasena.isEmpty()){
            //enviar mensaje de error y reenviar al usuario hacia alguna pantalla de comprovacion
        }else{
            if (networkInfo != null && networkInfo.isConnected()) {
                llenarperfiltrabajador(ruttrabajador);
            }else{
                //no hay conexion manejar excepcion

            }
        }



        btncrearsolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (networkInfo != null && networkInfo.isConnected()) {
                Bundle bundle = new Bundle();
                //en el bundle se enviaran los siguientes datos para poder operar en el crear soliciutd
                bundle.putString("ruttrabajador", trabajador.getRUT());
                bundle.putString("nombretrabajador", trabajador.getNombre()+" "+trabajador.getApellido());
                bundle.putString("estadotrabajador", trabajador.getEstado());
                bundle.putString("calificaciontrabajador", trabajador.getCalificacion());
                bundle.putString("imgperfiltrabajador", (String) urlfoto);
                bundle.putInt("idrubro", idrubro);
                crearsolicitudFragment crearsolicitudFragment = new crearsolicitudFragment();
                crearsolicitudFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.container,crearsolicitudFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
                }else{
                    //no hay conexion manejar excepcion

                }
            }
        });


        return  v;
    }

  private void llenarperfiltrabajador(String ruttrabajador) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://proyectotesis.ddns.net/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            tesisAPI tesisAPI = retrofit.create(com.example.practicadiseo.interfaces.tesisAPI.class);
            //metodo para llamar a la funcion que queramos
            //llamar a la funcion de get usuario la cual se le envia los datos (rut)
            Call<UsuarioTrabajador> call = tesisAPI.getUsuarioTrabajador(ruttrabajador,rutusuario,contrasena);
            call.enqueue(new Callback<UsuarioTrabajador>() {
                @Override
                public void onResponse( Call<UsuarioTrabajador>call, Response<UsuarioTrabajador> response) {
                    //si esta malo se ejecuta este trozo
                    if(!response.isSuccessful()){

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        LayoutInflater inflater = getLayoutInflater();
                        View viewsync = inflater.inflate(R.layout.alertdialogusuarioexistente,null);
                        builder.setView(viewsync);
                        AlertDialog dialog = builder.create();
                        dialog.setCancelable(false);
                        dialog.show();
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        TextView  texto=(TextView) viewsync.findViewById(R.id.txtalertnotificacion);
                        texto.setText("Ya exist una solicitud Sin completar con este trabajador. revise su lista de solicitudes.");
                        Button btnusuarioexiste = viewsync.findViewById(R.id.btnusuarioexiste);

                        btnusuarioexiste.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(), menuActivity.class);
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        });


                        Toast.makeText(getContext(), "error :"+response.code(), Toast.LENGTH_LONG).show();
                    }
                    //de lo contrario se ejecuta esta parte
                    else {
                        //respuesta del request y seteo de los valores del usuario encontrado en los campos de la vista
                        UsuarioTrabajador usuarios = response.body();
                        trabajador = usuarios;
                        rut.setText(usuarios.getRUT().toString());
                        nombre.setText(usuarios.getNombre().toString()+" "+usuarios.getApellido().toString());
                        telefono.setText(usuarios.getFono().toString());
                        ciudad.setText(usuarios.getCiudad().toString());
                        setestrellas(usuarios.getCalificacion());
                        estado.setText(usuarios.getEstado());
                        urlfoto =usuarios.getFoto();
                        loadingdots2.setVisibility(View.INVISIBLE);
                        loadingdots2.cancelAnimation();
                        Glide.with(getContext()).load(String.valueOf(rutaservidor+usuarios.getFoto())).into(foto);
                    }
                }
                //si falla el request a la pagina mostrara este error
                @Override
                public void onFailure(Call<UsuarioTrabajador> call, Throwable t) {
                    Toast.makeText(getContext(), "error :"+t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
}


    private void showSelectedFragment(Fragment fragment){
        getFragmentManager().beginTransaction().replace(R.id.container,fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

   private void  setestrellas(String calificaciona) {
        if(calificaciona.equals("5")){
            calificacion.setText("★★★★★");
        }
       if(calificaciona.equals("4")){
           calificacion.setText("★★★★");
       }
       if(calificaciona.equals("3")){
           calificacion.setText("★★★");
       }
       if(calificaciona.equals("2")){
           calificacion.setText("★★");
       }
       if(calificaciona.equals("1")){
           calificacion.setText("★");
       }
       if(calificaciona.equals("0")){
           calificacion.setText("No posee Calificacion");
       }
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



}
