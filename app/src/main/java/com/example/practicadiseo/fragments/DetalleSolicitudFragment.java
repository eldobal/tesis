package com.example.practicadiseo.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.practicadiseo.R;
import com.example.practicadiseo.clases.GlobalInfo;
import com.example.practicadiseo.clases.Solicitud;
import com.example.practicadiseo.interfaces.tesisAPI;
import com.google.android.material.snackbar.Snackbar;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetalleSolicitudFragment extends Fragment {
    SweetAlertDialog dp;
    Solicitud solicitud = new Solicitud();
    private TextView numerosolicitud,fechasolicitud,fechadetallesolicitud,cliente,trabajador,rubro,precio,estadosolicitud,descripciondetallesolicitud,diagnosticodetallesolicitud,soluciondetallesolicitud;
    private ImageView imgperfiltrabajador,imgclientesacada;
    SharedPreferences prefs;
    private Button btnpagarsolicitud;
    private int idsolicitud=0,montosolicitud=0;
    private CardView carddiagnostico,cardsolucion;
    final static String rutaservidor= "http://proyectotesis.ddns.net";
    private String rutperfil ="",contrasenaperfil="", metodopago="";
    NetworkInfo activeNetwork;
    ConnectivityManager cm ;


    public DetalleSolicitudFragment() {
        // Required empty public constructor
    }

    @Override
       public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        numerosolicitud = (TextView) getActivity().findViewById(R.id.txtnumerosolicitud);
        fechasolicitud = (TextView)getActivity().findViewById(R.id.txtfechasolicitud);
        fechadetallesolicitud = (TextView)getActivity().findViewById(R.id.txtfechadetallesolicitud);
        trabajador = (TextView)getActivity().findViewById(R.id.txttrabajadorsolicituddetalle);
        rubro = (TextView)getActivity().findViewById(R.id.txtrubrosolicituddetalle);
        precio = (TextView)getActivity().findViewById(R.id.txtpreciosolicitud);
        estadosolicitud =(TextView)getActivity().findViewById(R.id.txtestadosolicitud);
        descripciondetallesolicitud =(TextView)getActivity().findViewById(R.id.txtdescripciondetallesolicitud);
        diagnosticodetallesolicitud =(TextView)getActivity().findViewById(R.id.txtdiagnosticodetallesolicitud1);
        soluciondetallesolicitud =(TextView)getActivity().findViewById(R.id.txtsoluciondetallesolicitud);
        imgperfiltrabajador =(ImageView)getActivity().findViewById(R.id.imgperfilfilasolicitud);
        imgclientesacada =(ImageView)getActivity().findViewById(R.id.imgclientesacada);
        btnpagarsolicitud = (Button)getActivity().findViewById(R.id.btnpagarsolicitud);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detalle_solicitud, container, false);
        prefs = this.getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        setcredentiasexist();
        Bundle datosRecuperados = getArguments();
        if (datosRecuperados != null) {
            idsolicitud = datosRecuperados.getInt("idsolicitud");
        } else {
            //falta validacion sobre el id
        }
        btnpagarsolicitud = (Button) v.findViewById(R.id.btnpagarsolicitud);
        carddiagnostico = (CardView) v.findViewById(R.id.carddiagnosticodetalle);
        cardsolucion = (CardView) v.findViewById(R.id.cardsolucionsolicitud);

        cargardetallesolicitud();

        btnpagarsolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();
                View viewsync = inflater.inflate(R.layout.alerdialogpagarsolicitud,null);
                builder.setView(viewsync);
                AlertDialog dialog2 = builder.create();
                dialog2.setCancelable(false);
                dialog2.show();
                dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView texto = (TextView) viewsync.findViewById(R.id.txtalertnotificacion);
                texto.setText("Ingrese el metodo con el cual usted realizara el pago. Califique al trabajador brevemente");
                final RadioGroup group= (RadioGroup) viewsync.findViewById(R.id.radiogroup);
                RadioButton radioButtonefectivo = (RadioButton) viewsync.findViewById(R.id.radioButtonefectivo);
                RadioButton radioButtononline = (RadioButton) viewsync.findViewById(R.id.radioButtononline);
                RatingBar ratingBar = (RatingBar) viewsync.findViewById(R.id.ratingbarcalificacion);
                EditText comentariocalidicacion = (EditText) viewsync.findViewById(R.id.edittextcomentariocalificacion);
                Button btndismiss = (Button) viewsync.findViewById(R.id.btncerraralert);
                Button btnpagar = (Button) viewsync.findViewById(R.id.btnfinalizarsolicitud);

                group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        int id = group.getCheckedRadioButtonId();
                        switch (id) {
                            case R.id.radioButtonefectivo:
                                metodopago="EFECTIVO";
                                break;
                            case R.id.radioButtononline:
                                metodopago="ONLINE";
                                break;
                        }
                    }
                });

                btndismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog2.dismiss(); }
                });

                btnpagar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(radioButtonefectivo.isChecked()==false && radioButtononline.isChecked()==false){
                            Toast.makeText(v.getContext(), "seleccione una opcion por favor.", Toast.LENGTH_LONG).show();
                        }else{
                            //comentario de calificacion
                            String comentario = comentariocalidicacion.getText().toString();
                            if(comentario.length() <300){
                                if(ratingBar.getRating() != 0 ){
                                    if(radioButtonefectivo.isChecked() == true) {

                                        cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                                        activeNetwork = cm.getActiveNetworkInfo();
                                        if (activeNetwork != null) {
                                            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                                                //todo el codigo que e ejectuta cuando la trnasaccion se realiza en enfectivo
                                                int calificacion = Math.round(ratingBar.getRating());

                                                //se llama al metodo que confirma el pago por parte del cliente
                                                Retrofit retrofit = new Retrofit.Builder()
                                                        .baseUrl(GlobalInfo.Rutaservidor)
                                                        .addConverterFactory(GsonConverterFactory.create())
                                                        .build();
                                                tesisAPI tesisAPI = retrofit.create(com.example.practicadiseo.interfaces.tesisAPI.class);
                                                Call<String> call = tesisAPI.pagarCliente(rutperfil, contrasenaperfil, idsolicitud, metodopago, calificacion, comentario);
                                                call.enqueue(new Callback<String>() {
                                                    @Override
                                                    public void onResponse(Call<String> call, Response<String> response) {
                                                        //si esta malo se ejecuta este trozo
                                                        if (!response.isSuccessful()) {
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                            LayoutInflater inflater = getLayoutInflater();
                                                            View viewsync = inflater.inflate(R.layout.alerdialogerrorresponce, null);
                                                            builder.setView(viewsync);
                                                            AlertDialog dialog5 = builder.create();
                                                            dialog5.setCancelable(false);
                                                            dialog5.show();
                                                            dialog5.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                            TextView texto = (TextView) viewsync.findViewById(R.id.txtalertnotificacion);
                                                            texto.setText("Ha ocurrido un error al momento de pagar. intente en un momento nuevamente.");
                                                            Button btncerrar = (Button) viewsync.findViewById(R.id.btnalertperfilexito);

                                                            btncerrar.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    dialog5.dismiss();
                                                                }
                                                            });

                                                           // Toast.makeText(getContext(), "error/detalle/finalizar/onresponse :" + response.code(), Toast.LENGTH_LONG).show();
                                                        }
                                                        //de lo contrario se ejecuta esta parte
                                                        else {
                                                            String respusta = response.body();

                                                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                            View viewsync2 = inflater.inflate(R.layout.alertdialogperfilactualizado, null);
                                                            builder.setView(viewsync2);
                                                            AlertDialog dialog3 = builder.create();
                                                            dialog3.setCancelable(false);
                                                            dialog3.show();
                                                            dialog3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                            Button btncerraralert = viewsync2.findViewById(R.id.btnalertperfilexito);
                                                            TextView texto = (TextView) viewsync2.findViewById(R.id.txtalertnotificacion);
                                                            texto.setText("Ha confirmado su pago Satisfactoriamente.");
                                                            btncerraralert.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View view) {
                                                                    solicitudeFragment solicitudesFragment = new solicitudeFragment();
                                                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, solicitudesFragment, "solicitudtag")
                                                                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                                                            //permite regresar hacia atras entre los fragments
                                                                            //.addToBackStack(null)
                                                                            .commit();
                                                                    dialog3.dismiss();
                                                                    dialog2.dismiss();
                                                                }
                                                            });
                                                        }
                                                    }

                                                    //si falla el request a la pagina mostrara este error
                                                    @Override
                                                    public void onFailure(Call<String> call, Throwable t) {
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                        LayoutInflater inflater = getLayoutInflater();
                                                        View viewsync = inflater.inflate(R.layout.alerdialogerrorservidor, null);
                                                        builder.setView(viewsync);
                                                        AlertDialog dialog6 = builder.create();
                                                        dialog6.setCancelable(false);
                                                        dialog6.show();
                                                        dialog6.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                        TextView texto = (TextView) viewsync.findViewById(R.id.txterrorservidor);
                                                        texto.setText("Ha ocurrido un error con la coneccion del servidor, Estamos trabajando para solucionarlo.");
                                                        Button btncerrar = (Button) viewsync.findViewById(R.id.btncerraralert);

                                                        btncerrar.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                dialog6.dismiss();
                                                            }
                                                        });

                                                    //    Toast.makeText(getContext(), "error/detalle/finalizar/onfailure:" + t.getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                });


                                            } else {
                                                //alerd no coneccion
                                                Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                                                        "No se ha encontrado una coneccion a Internet.", Snackbar.LENGTH_LONG);
                                                snackBar.show();
                                            }
                                        }

                                    }if(radioButtononline.isChecked() ==true){
                                        int calificacion = Math.round(ratingBar.getRating());
                                        //todo el codigo que se ejecuta cuando se pagara con webpay
                                        Bundle bundle = new Bundle();
                                        //id de la solicitud para que se pueda buscar en el detalle
                                        bundle.putString("rutusuario",rutperfil);
                                        bundle.putInt("monto",montosolicitud);
                                        bundle.putInt("idsolicitud", idsolicitud);
                                        bundle.putInt("calificacion",calificacion);
                                        bundle.putString("comentario",comentario);
                                        onepayFragment onepayFragment = new onepayFragment();
                                        onepayFragment.setArguments(bundle);

                                        getFragmentManager().beginTransaction().replace(R.id.container,onepayFragment)
                                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                                .commit();

                                        dialog2.dismiss();
                                    }


                                }else{
                                    Toast.makeText(v.getContext(), "seleccione una calificacion valida.", Toast.LENGTH_LONG).show();
                                }

                            }else{
                                //comentario de mas de 300 caracteres
                                Toast.makeText(v.getContext(), "El comentario no debe ser mayor a 300 caracteres.", Toast.LENGTH_LONG).show();
                            }

                        }

                    }
                });


            }
        });


        return v;
    }

    private void cargardetallesolicitud() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GlobalInfo.Rutaservidor)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        tesisAPI tesisAPI = retrofit.create(com.example.practicadiseo.interfaces.tesisAPI.class);
        Call<Solicitud> call = tesisAPI.getSolicitudCliente(idsolicitud,rutperfil,contrasenaperfil);
        call.enqueue(new Callback<Solicitud>() {
            @Override
            public void onResponse(Call<Solicitud> call, Response<Solicitud> response) {
                if(!response.isSuccessful()){
                    //falta hacer un alert
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    LayoutInflater inflater = getLayoutInflater();
                    View viewsync = inflater.inflate(R.layout.alerdialogerrorresponce,null);
                    builder.setView(viewsync);
                    AlertDialog dialog = builder.create();
                    dialog.setCancelable(false);
                    dialog.show();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    TextView texto = (TextView) viewsync.findViewById(R.id.txtalertnotificacion);
                    texto.setText("Ha ocurrido un error con la respuesta al traer el detalle. intente en un momento nuevamente.");
                    Button btncerrar =(Button) viewsync.findViewById(R.id.btnalertperfilexito);

                    btncerrar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                  //Toast.makeText(getContext(), "error :"+response.code(), Toast.LENGTH_LONG).show();
                }
                else {
                    Solicitud solicituds = response.body();
                    if(solicituds.getEstado().equals("FINALIZANDO")){
                        btnpagarsolicitud.setVisibility(View.VISIBLE);
                    }else{
                        btnpagarsolicitud.setVisibility(View.GONE);
                    }
                    //cuando la solicitud este en estado atendiendo no se mostraran la solucion y el diagnosticos.
                    if(solicituds.getEstado().equals("ATENDIENDO")){
                        carddiagnostico.setVisibility(View.GONE);
                        cardsolucion.setVisibility(View.GONE);
                    }
                    //estados en los cuales se muestran la solucion y el diagnostico
                    if(solicituds.getEstado().equals("FINALIZANDO") || solicituds.getEstado().equals("FINALIZADO") || solicituds.getEstado().equals("COMPLETADA Y PAGADA") || solicituds.getEstado().equals("COMPLETADA Y NO PAGADA") )
                    {
                        carddiagnostico.setVisibility(View.GONE);
                        cardsolucion.setVisibility(View.VISIBLE);
                        String solucion = solicituds.getSolucion();
                        if(solucion == null){
                            soluciondetallesolicitud.setText("El trabajador con el Rut : "+solicituds.getRUT()+" no ha dejado ninguna solucion u observacion.");
                        }else{
                            soluciondetallesolicitud.setText(solicituds.getSolucion());
                        }
                    }
                    numerosolicitud.setText("N Solicitud: "+solicituds.getIdSolicitud());
                    fechasolicitud.setText("Creada: "+solicituds.getFechaS());
                    fechadetallesolicitud.setText("Atendida: "+solicituds.getFechaA());
                    trabajador.setText("Rut trabajador: "+solicituds.getRUT());
                    rubro.setText("Rubro: "+solicituds.getRubro());
                    if(solicituds.getEstado().equals("COMPLETADA Y PAGADA") || solicituds.getEstado().equals("COMPLETADA Y NO PAGADA")){
                        precio.setText("Precio Final: "+solicituds.getPrecio());
                    }else {
                        precio.setText("Precio aprox: " + solicituds.getPrecio());
                    }
                    montosolicitud = solicituds.getPrecio();
                    estadosolicitud.setText("Estado : "+solicituds.getEstado());
                    descripciondetallesolicitud.setText(solicituds.getDescripcionP());
                    diagnosticodetallesolicitud.setText(solicituds.getDiagnostico());
                    //carga de la foto del trabajor
                    Glide.with(getContext()).load(String.valueOf(rutaservidor+solicituds.getFotoT())).into(imgperfiltrabajador);
                    //carga de foto cargada por el usuario
                    Glide.with(getContext()).load(String.valueOf(rutaservidor+solicituds.getIdFoto())).into(imgclientesacada);
                }
            }
            @Override
            public void onFailure(Call<Solicitud> call, Throwable t) {
              //  Toast.makeText(getContext(), "error :"+t.getMessage(), Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();
                View viewsync = inflater.inflate(R.layout.alerdialogerrorservidor,null);
                builder.setView(viewsync);
                AlertDialog dialog4 = builder.create();
                dialog4.setCancelable(false);
                dialog4.show();
                dialog4.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView texto = (TextView) viewsync.findViewById(R.id.txterrorservidor);
                texto.setText("Ha ocurrido un error con la coneccion del servidor, Estamos trabajando para solucionarlo.");
                Button btncerrar =(Button) viewsync.findViewById(R.id.btncerraralert);
                btncerrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog4.dismiss();
                    }
                });

            }
        });

    }

    private void showSelectedFragment(Fragment fragment){
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment)
                .replace(R.id.container,fragment)
                //permite regresar hacia atras entre los fragments
                .addToBackStack(null)
                .commit();
    }

    private void setcredentiasexist() {
        String rutq = getuserrutprefs();
        String contrasena = getusercontraseñaprefs();
        if (!TextUtils.isEmpty(rutq)&& (!TextUtils.isEmpty(contrasena)) ) {
            rutperfil=rutq.toString();
            contrasenaperfil=contrasena.toString();
        }
    }

    private String getuserrutprefs() {
        return prefs.getString("Rut", "");
    }

    private String getusercontraseñaprefs() {
        return prefs.getString("ContraseNa", "");
    }



}
