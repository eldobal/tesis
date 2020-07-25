package com.example.practicadiseo.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cl.ionix.tbk_ewallet_sdk_android.OnePay;
import cl.ionix.tbk_ewallet_sdk_android.callback.OnePayCallback;

import com.example.practicadiseo.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link onepayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class onepayFragment extends Fragment {
    WebView webView;
    NetworkInfo NetworkInfo;
    String rutusuario="";
    int monto=0,idsolicitud=0;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public onepayFragment() {
        // Required empty public constructor
    }


    public static onepayFragment newInstance(String param1, String param2) {
        onepayFragment fragment = new onepayFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo = connectivityManager.getActiveNetworkInfo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_onepay, container, false);
        //se requieren recibir los datos claves(Monto,idsolicitud) para luego ser enviados al webview con la interfaz de webpay
        Bundle datosRecuperados = getArguments();
        if (datosRecuperados != null) {
            idsolicitud = datosRecuperados.getInt("idsolicitud");
            monto = datosRecuperados.getInt("monto");
            rutusuario = datosRecuperados.getString("rutusuario");
        }else{
            //falta validacion sobre el id
            HomeFragment homeFragment  = new HomeFragment();
            getFragmentManager().beginTransaction().replace(R.id.container,homeFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
            getActivity().finish();
        }
        webView = (WebView) v.findViewById(R.id.webview);
        //este codigo habilia javascript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //url del backend webpay
        String url="http://proyectotesis.ddns.net/datos/WebPay";
        String postData= null;
        String idsoli = Integer.toString(idsolicitud);
        String montosoli = Integer.toString(monto);

        try {
            postData = "RUT="+ URLEncoder.encode(rutusuario,"UTF8")+
                            "&idSolicitud="+ URLEncoder.encode(idsoli, "UTF-8")+
                            "&Monto=" +URLEncoder.encode(montosoli,"UTF-8")

            ;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        webView.postUrl(url,postData.getBytes());

        //url la cual estara alojado el backend con la implementacion de transbank
        String urltrasaccionfinalizada ="http://proyectotesis.ddns.net/Datos/Final";

        //metodo en el cual el timer pregunta frecuentemente si la urlactual es igual a la urffinalizada
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        //se captura la url donde se encuentra el usuario en el webview
                        String webUrlactual = webView.getUrl();
                        if(urltrasaccionfinalizada.equals(webUrlactual) && NetworkInfo.isConnected()) {
                            //Ejecuta tu AsyncTask!
                            //alert dialog con el mensaje de que se ha pagado correctamente
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            LayoutInflater inflater = getLayoutInflater();
                            View viewsync = inflater.inflate(R.layout.alertdialogwebpaypago,null);
                            builder.setView(viewsync);
                            AlertDialog dialog7 = builder.create();
                            dialog7.setCancelable(false);
                            dialog7.show();
                            dialog7.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            Button btncerrar =(Button) viewsync.findViewById(R.id.btnalertperfilexito);

                            btncerrar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog7.dismiss();
                                    //salir de esta pantalla
                                    getActivity().finish();
                                    HomeFragment homeFragment = new HomeFragment();
                                    getFragmentManager().beginTransaction().replace(R.id.container,homeFragment)
                                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                            .commit();

                                }
                            });
                        }
                    }
                });
            }
        };
        timer.schedule(task, 0, 2000);  //ejecutar en intervalo definido por el programador


        return  v;
    }
}