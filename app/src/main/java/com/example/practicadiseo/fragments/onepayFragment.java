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
import android.view.animation.Animation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cl.ionix.tbk_ewallet_sdk_android.OnePay;
import cl.ionix.tbk_ewallet_sdk_android.callback.OnePayCallback;

import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieAnimationView;
import com.example.practicadiseo.R;
import com.example.practicadiseo.activitys.menuActivity;
import com.google.android.material.snackbar.Snackbar;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link onepayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class onepayFragment extends Fragment  {
    WebView webView;
    NetworkInfo NetworkInfo;
    String rutusuario="",comentario="";
    int monto=0,idsolicitud=0,calificacion=0;
    NetworkInfo activeNetwork;
    ConnectivityManager cm ;
    TimerTask task;


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
       // ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
       // NetworkInfo = connectivityManager.getActiveNetworkInfo();
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
            calificacion = datosRecuperados.getInt("calificacion");
            comentario= datosRecuperados.getString("comentario");
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
        webSettings = webView.getSettings(); webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true); webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true); webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false); webSettings.setSupportZoom(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);




        //url del backend webpay
        String url="http://proyectotesis.ddns.net/datos/WebPay";
        String postData= null;
        String idsoli = Integer.toString(idsolicitud);
        String montosoli = Integer.toString(monto);
        String Calificacion= Integer.toString(calificacion);

        try {
            postData = "RUT="+ URLEncoder.encode(rutusuario,"UTF8")+
                            "&idSolicitud="+ URLEncoder.encode(idsoli, "UTF-8")+
                            "&Monto=" +URLEncoder.encode(montosoli,"UTF-8")+
                            "&Calificacion=" +URLEncoder.encode(Calificacion,"UTF-8")+
                            "&Comentario=" +URLEncoder.encode(comentario,"UTF-8")

            ;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                webView.postUrl(url, postData.getBytes());

            } else {

            }
        }

        //url la cual estara alojado el backend con la implementacion de transbank
        String urltrasaccionfinalizada ="http://proyectotesis.ddns.net/Datos/Final";
        String urltransaccionerror  ="http://proyectotesis.ddns.net/Datos/Error";

        //metodo en el cual el timer pregunta frecuentemente si la urlactual es igual a la urffinalizada
        final Handler handler = new Handler();
        Timer timer = new Timer();
         task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                        activeNetwork = cm.getActiveNetworkInfo();
                        if (activeNetwork != null) {
                            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                                //se captura la url donde se encuentra el usuario en el webview
                                String webUrlactual = webView.getUrl();
                             //   Toast.makeText(getContext(), webUrlactual, Toast.LENGTH_SHORT).show();
                                if (urltrasaccionfinalizada.equals(webUrlactual)) {
                                    task.cancel();
                                    //alert dialog con el mensaje de que se ha pagado correctamente
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    LayoutInflater inflater = getLayoutInflater();
                                    View viewsync = inflater.inflate(R.layout.alertdialogwebpaypago, null);
                                    builder.setView(viewsync);
                                    AlertDialog dialog7 = builder.create();
                                    dialog7.setCancelable(false);
                                    dialog7.show();
                                    dialog7.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    Button btncerrar = (Button) viewsync.findViewById(R.id.btnalertperfilexito);
                                    btncerrar.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog7.dismiss();
                                            //salir de esta pantalla
                                            HomeFragment homeFragment = new HomeFragment();
                                            getFragmentManager().beginTransaction().replace(R.id.container, homeFragment)
                                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                                    .commit();
                                        }
                                    });
                                }
                                if(urltransaccionerror.equals(webUrlactual)){
                                    task.cancel();
                                    //alert dialog con el mensaje de que se ha pagado correctamente
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    LayoutInflater inflater = getLayoutInflater();
                                    View viewsync = inflater.inflate(R.layout.alertdialogwebpaypago, null);
                                    builder.setView(viewsync);
                                    AlertDialog dialog8 = builder.create();
                                    dialog8.setCancelable(false);
                                    dialog8.show();
                                    dialog8.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    LottieAnimationView lottieAnimationView = (LottieAnimationView) viewsync.findViewById(R.id.idanimacionpagowebpay);
                                    lottieAnimationView.setAnimation(R.raw.pagoincorrecto);
                                    TextView texto = (TextView) viewsync.findViewById(R.id.txtalertnotificacion);
                                    texto.setText("Lamentablemente el pago ha sido rechazado. por fabor intentelo nuevamente!. en caso de que el problema persista por favor pagar en efectivo o comunicarse con la linea de atencion");
                                    Button btncerrar = (Button) viewsync.findViewById(R.id.btnalertperfilexito);
                                    btncerrar.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog8.dismiss();
                                            solicitudeFragment solicitudeFragment = new solicitudeFragment();
                                            getFragmentManager().beginTransaction().replace(R.id.container, solicitudeFragment,"solicitudtag")
                                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                                    .commit();
                                        }
                                    });
                                }
                            } else {
                                Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                                        "No se ha encontrado una coneccion a Internet.", Snackbar.LENGTH_LONG);
                                snackBar.show();
                            }
                        }
                    }
                });
            }
        };
        timer.schedule(task, 0, 500);  //ejecutar en intervalo definido por el programador


        return  v;
    }


}