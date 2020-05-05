package com.example.practicadiseo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Intent.getIntent;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetalleSolicitudFragment extends Fragment {
    SweetAlertDialog dp;

    Solicitud solicitud = new Solicitud();

    private TextView numerosolicitud;
    private TextView fechasolicitud;
    private TextView fechadetallesolicitud;
    private TextView cliente;
    private TextView trabajador;
    private TextView rubro;
    private TextView precio;
    private TextView estadosolicitud;
    private TextView descripciondetallesolicitud;
    private TextView diagnosticodetallesolicitud;
    private TextView soluciondetallesolicitud;

    private ImageView imgperfil;
    SharedPreferences prefs;
    private Button btnvolver;
    private int idsolicitud=0;

    public DetalleSolicitudFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_detalle_solicitud, container, false);
         prefs = this.getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);


        numerosolicitud = (TextView) v.findViewById(R.id.txtnumerosolicitud);
        fechasolicitud = (TextView)v.findViewById(R.id.txtfechasolicitud);
        fechadetallesolicitud = (TextView)v.findViewById(R.id.txtfechadetallesolicitud);

        trabajador = (TextView)v.findViewById(R.id.txttrabajadorsolicituddetalle);
        rubro = (TextView)v.findViewById(R.id.txtrubrosolicituddetalle);
        precio = (TextView)v.findViewById(R.id.txtpreciosolicitud);
        estadosolicitud =(TextView)v.findViewById(R.id.txtestadosolicitud);

        descripciondetallesolicitud =(TextView)v.findViewById(R.id.txtdescripciondetallesolicitud);
        diagnosticodetallesolicitud =(TextView)v.findViewById(R.id.txtdiagnosticodetallesolicitud1);
        soluciondetallesolicitud =(TextView)v.findViewById(R.id.txtsoluciondetallesolicitud);



        imgperfil =(ImageView)v.findViewById(R.id.imgperfilfilasolicitud);
        btnvolver = (Button)v.findViewById(R.id.btnvolver);




        Bundle datosRecuperados = getArguments();
        if (datosRecuperados != null) {
            idsolicitud = datosRecuperados.getInt("idsolicitud");
        }




        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://proyectotesis.ddns.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        tesisAPI tesisAPI = retrofit.create(com.example.practicadiseo.tesisAPI.class);

        Call<Solicitud> call = tesisAPI.getSolicitudCliente(idsolicitud);

        call.enqueue(new Callback<Solicitud>() {
            @Override
            public void onResponse(Call<Solicitud> call, Response<Solicitud> response) {

                if(!response.isSuccessful()){
                    Toast.makeText(v.getContext(), "error :"+response.code(), Toast.LENGTH_LONG).show();
                }
                else {
                    Solicitud solicituds = response.body();

                    numerosolicitud.setText("N Solicitud: "+solicituds.getIdSolicitud());
                    fechasolicitud.setText("Creada: "+solicituds.getFechaS());
                    fechadetallesolicitud.setText("Atendida: "+solicituds.getFechaA());
                    trabajador.setText("Rut trabajador: "+solicituds.getRUT());
                    rubro.setText("Rubro: "+solicituds.getRubro());
                    precio.setText("Precio aprox: "+solicituds.getPrecio());
                    estadosolicitud.setText("Estado : "+solicituds.getEstado());

                    descripciondetallesolicitud.setText(solicituds.getDescripcionP());
                    diagnosticodetallesolicitud.setText(solicituds.getDiagnostico());
                    soluciondetallesolicitud.setText(solicituds.getSolucion());
                }

            }

            @Override
            public void onFailure(Call<Solicitud> call, Throwable t) {
                Toast.makeText(v.getContext(), "error :"+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });



        return v;
    }




}
