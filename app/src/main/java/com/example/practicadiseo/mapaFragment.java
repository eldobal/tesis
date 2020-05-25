package com.example.practicadiseo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;


/**
 * A simple {@link Fragment} subclass.
 */
public class mapaFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap map;
    boolean actualposition= true;
    double longitudorigen, latitudorigen;
    SharedPreferences prefsmaps;
    Button btnconfirmarubicacion;
    public mapaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_mapa, container, false);
        prefsmaps = this.getActivity().getSharedPreferences("ubicacionmapa", Context.MODE_PRIVATE);
        btnconfirmarubicacion=(Button) v.findViewById(R.id.btnconfirmarubicacion);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

                       if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }


            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }


        }
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.setMyLocationEnabled(true);


        btnconfirmarubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //colocar aleta con sweet alert dialog

                if(latitudorigen!= 0.0 && longitudorigen!=0.0){
                    saveOnPreferences(latitudorigen,longitudorigen);
                    getActivity().onBackPressed();
                }else {
                    Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                            "Ingrese Una direccion valida", Snackbar.LENGTH_LONG);
                    snackBar.show();
                }


            }
        });

        map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                if(actualposition){
                    latitudorigen= location.getLatitude();
                    longitudorigen= location.getLongitude();
                    actualposition=false;

                    LatLng miposicion = new LatLng(latitudorigen,longitudorigen);

                    map.addMarker(new MarkerOptions().position(miposicion).title("estoy aqui!"));


                    map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            map.clear();
                            latitudorigen=0;
                            longitudorigen=0;
                            //se crea el marcador
                            MarkerOptions markerOptions = new MarkerOptions();
                            //se setea la posicion al marcador
                            markerOptions.position(latLng);

                            latitudorigen=latLng.latitude;
                            longitudorigen=latLng.longitude;
                            //descripcion del titulo
                            markerOptions.title(latLng.latitude+" ; "+latLng.longitude);
                            //borrar los click anteriors
                            map.clear();

                            //zoom al marcador
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17));
                            //añadir el marcador al mapa
                            map.addMarker(markerOptions);

                        }
                    });


                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    private void saveOnPreferences(Double latitud, Double longitud) {
        SharedPreferences.Editor editor = prefsmaps.edit();

        //putDoublelatitud(prefsmaps.edit(),"latitud",latitud);
        //putDoublelongirud(prefsmaps.edit(),"longitud",longitud);
        editor.putString("Latitud",latitud.toString());
        editor.putString("Longitud",longitud.toString());
        //linea la cual guarda todos los valores en la pref antes de continuar
        editor.commit();
        editor.apply();
    }


    SharedPreferences.Editor putDoublelatitud(final SharedPreferences.Editor edit, final String key, final double value) {
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }


    private double getDoublelatitud(final SharedPreferences prefs, final String key, final double defaultValue) {
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }


    SharedPreferences.Editor putDoublelongirud(final SharedPreferences.Editor edit, final String key, final double value) {
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }


    private double getDoublelongitud(final SharedPreferences prefs, final String key, final double defaultValue) {
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }


    public interface OnFragmentInteractionListener {
    }
}
