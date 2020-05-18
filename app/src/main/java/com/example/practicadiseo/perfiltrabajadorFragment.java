package com.example.practicadiseo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

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
    private Button btncrearsolicitud;
    private ImageView foto;
    final static String rutaservidor= "http://proyectotesis.ddns.net";
    String urlfoto="";

    public perfiltrabajadorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_perfiltrabajador, container, false);
        Bundle args = getArguments();
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

        btncrearsolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                //en el bundle se enviaran los siguientes datos para poder operar en el crear soliciutd
                bundle.putString("ruttrabajador", ruttrabajador);
                bundle.putString("nombretrabajador", (String) nombre.getText());
                bundle.putString("estadotrabajador", (String) estado.getText());
                bundle.putString("calificaciontrabajador", (String) calificacion.getText());
                bundle.putString("imgperfiltrabajador", (String) urlfoto);
                bundle.putInt("idrubro", idrubro);
                crearsolicitudFragment crearsolicitudFragment = new crearsolicitudFragment();
                crearsolicitudFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.container,crearsolicitudFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
            }
        });

        if(ruttrabajador.isEmpty()){
            //enviar mensaje de error y reenviar al usuario hacia alguna pantalla de comprovacion
        }else{
            llenarperfiltrabajador(ruttrabajador);
        }

        return  v;
    }

  private void llenarperfiltrabajador(String ruttrabajador) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://proyectotesis.ddns.net/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            tesisAPI tesisAPI = retrofit.create(com.example.practicadiseo.tesisAPI.class);
            //metodo para llamar a la funcion que queramos
            //llamar a la funcion de get usuario la cual se le envia los datos (rut)
            Call<UsuarioTrabajador> call = tesisAPI.getUsuarioTrabajador(ruttrabajador);
            call.enqueue(new Callback<UsuarioTrabajador>() {
                @Override
                public void onResponse( Call<UsuarioTrabajador>call, Response<UsuarioTrabajador> response) {
                    //si esta malo se ejecuta este trozo
                    if(!response.isSuccessful()){
                        Toast.makeText(getContext(), "error :"+response.code(), Toast.LENGTH_LONG).show();
                    }
                    //de lo contrario se ejecuta esta parte
                    else {
                        //respuesta del request y seteo de los valores del usuario encontrado en los campos de la vista
                        UsuarioTrabajador usuarios = response.body();
                        rut.setText("Rut: "+usuarios.getRUT().toString());
                        nombre.setText("Trabajdor: "+usuarios.getNombre().toString()+" "+usuarios.getApellido().toString());
                        telefono.setText("Telefono: "+usuarios.getFono().toString());
                        ciudad.setText("Ciudad: "+usuarios.getCiudad().toString());
                        setestrellas(usuarios.getCalificacion());
                        estado.setText("Estado Trabajador: "+usuarios.getEstado());
                        urlfoto =usuarios.getFoto();
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
            calificacion.setText("Calificacion: ★★★★★");
        }
       if(calificaciona.equals("4")){
           calificacion.setText("Calificacion: ★★★★");
       }
       if(calificaciona.equals("3")){
           calificacion.setText("Calificacion: ★★★");
       }
       if(calificaciona.equals("2")){
           calificacion.setText("Calificacion: ★★");
       }
       if(calificaciona.equals("1")){
           calificacion.setText("Calificacion: ★");
       }
       if(calificaciona.equals("0")){
           calificacion.setText("Calificacion: No posee Calificacion");
       }
   }
}
