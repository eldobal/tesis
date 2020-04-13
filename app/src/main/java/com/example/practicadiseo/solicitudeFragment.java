package com.example.practicadiseo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.DateTimeKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class solicitudeFragment extends Fragment {

    SweetAlertDialog dp;
    private static LayoutInflater inflater = null;
    private TextView tvNoRegistros;
    private ListView lista;
    private ImageButton btnVolver;

    ArrayList<Solicitud> solicitudes = new ArrayList<Solicitud>();
    public solicitudeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_solicitudes, container, false);




        return v;
    }
}
