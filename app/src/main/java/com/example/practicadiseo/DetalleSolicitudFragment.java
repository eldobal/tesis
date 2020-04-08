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
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


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

    private Button btnvolver;

    public DetalleSolicitudFragment() {
        // Required empty public constructor
    }

    String tecnico = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v= inflater.inflate(R.layout.fragment_detalle_solicitud, container, false);
        SharedPreferences prefs = this.getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        int img[] = {R.drawable.user};

        numerosolicitud = (TextView) v.findViewById(R.id.txtnumerosolicitud);
        fechasolicitud = (TextView)v.findViewById(R.id.txtfechasolicitud);
        fechadetallesolicitud = (TextView)v.findViewById(R.id.txtfechadetallesolicitud);
        cliente = (TextView)v.findViewById(R.id.txtclientesolicituddetalle);
        trabajador = (TextView)v.findViewById(R.id.txttrabajadorsolicituddetalle);
        rubro = (TextView)v.findViewById(R.id.txtrubrosolicituddetalle);
        precio = (TextView)v.findViewById(R.id.txtpreciosolicitud);
        estadosolicitud =(TextView)v.findViewById(R.id.txtestadosolicitud);

        descripciondetallesolicitud =(TextView)v.findViewById(R.id.txtdescripciondetallesolicitud);
        diagnosticodetallesolicitud =(TextView)v.findViewById(R.id.txtdiagnosticodetallesolicitud);
        soluciondetallesolicitud =(TextView)v.findViewById(R.id.txtsoluciondetallesolicitud);



        imgperfil =(ImageView)v.findViewById(R.id.imgperfilfilasolicitud);
        btnvolver = (Button)v.findViewById(R.id.btnvolver);



        Intent intent =;
        Bundle b = intent.getExtras();

        final Solicitud soli = (Solicitud) b.getParcelable("soli");


        numerosolicitud.setText(String.valueOf(soli.getIdSolicitud()));
        fechasolicitud.setText(soli.getFecha());
        descripciondetallesolicitud.setText(soli.getDescripcion());
        cliente.setText(String.valueOf(soli.getRut_Cliente()));
        trabajador.setText(soli.getRut_Trabajador());
        imgperfil.setImageResource(img[0]);



        return v;

    }

    String url = "http://www.sebastianbaldovinos.com/app/test.php?funcion=atendersolicitud";

    //metodo para enviar los datos y realizar la accion o acciones necesarias
    private void enviarRequest(final String idSolicitud, final String Tecnico, final String Usuario) {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String valor = jsonObject.getString("response");
                            if (valor.equals("OK")) {

                            } else {

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getMessage();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idSolicitud", idSolicitud);
                params.put("Atendidopor_Usuario", Tecnico);
                params.put("Solicitadopor_Usuario", Usuario);
                return params;
            }
        };
        AppSingleton.getInstance(.DetalleSolicitudFragment.this).addToRequestQue(stringRequest);
    }


}
