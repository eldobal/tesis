package com.example.practicadiseo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Solicitud implements Parcelable {

    private int idSolicitud;
    private String Fecha;
    private String Descripcion;
    private int Estado_idEstado;
    private String Solicitadopor_Usuario;
    private String Atendidopor_Usuario;

    private ArrayList<Solicitud> lista = new ArrayList<Solicitud>();


    public Solicitud() {
    }

    public Solicitud(Parcel in){
        lista=new ArrayList<Solicitud>();
        readFromParcel(in);
    }

    public int describeContents(){
        return 0;
    }

    private void readFromParcel(Parcel in){
        idSolicitud=in.readInt();
        Fecha=in.readString();
        Descripcion=in.readString();
        Estado_idEstado=in.readInt();
        Solicitadopor_Usuario= in.readString();
        Atendidopor_Usuario= in.readString();
        in.readTypedList(lista,CREATOR);
    }
    public void writeToParcel(Parcel dest, int flags){
        dest.writeInt(idSolicitud);
        dest.writeString(Fecha);
        dest.writeString(Descripcion);
        dest.writeInt(Estado_idEstado);
        dest.writeString(Solicitadopor_Usuario);
        dest.writeString(Atendidopor_Usuario);
        dest.writeTypedList(lista);
    }

    public void Add(Solicitud solicitudes)
    {
        lista.add(solicitudes);
    }

    public static final Creator<Solicitud>CREATOR =new Creator<Solicitud>(){
        public Solicitud createFromParcel(Parcel in){
            return new Solicitud(in);
        }
        public Solicitud[] newArray(int size){
            return new Solicitud[size];
        }
    };

    public Solicitud(int idSolicitud, String fecha, String descripcion, int estado_idEstado, String solicitadopor_Usuario, String atendidopor_Usuario) {
        this.idSolicitud = idSolicitud;
        this.Fecha = fecha;
        this.Descripcion = descripcion;
        this.Estado_idEstado = estado_idEstado;
        this.Solicitadopor_Usuario = solicitadopor_Usuario;
        this.Atendidopor_Usuario = atendidopor_Usuario;
    }

    public Solicitud(Solicitud solicitud) {
        this.idSolicitud = solicitud.idSolicitud;
        this.Fecha = solicitud.Fecha;
        this.Descripcion = solicitud.Descripcion;
        this.Estado_idEstado = solicitud.Estado_idEstado;
        this.Solicitadopor_Usuario = solicitud.Solicitadopor_Usuario;
        this.Atendidopor_Usuario = solicitud.Atendidopor_Usuario;
    }

    public int getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(int idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public int getEstado_idEstado() {
        return Estado_idEstado;
    }

    public void setEstado_idEstado(int estado_idEstado) {
        Estado_idEstado = estado_idEstado;
    }

    public String getSolicitadopor_Usuario() {
        return Solicitadopor_Usuario;
    }

    public void setSolicitadopor_Usuario(String solicitadopor_Usuario) {
        Solicitadopor_Usuario = solicitadopor_Usuario;
    }

    public String getAtendidopor_Usuario() {
        return Atendidopor_Usuario;
    }

    public void setAtendidopor_Usuario(String atendidopor_Usuario) {
        Atendidopor_Usuario = atendidopor_Usuario;
    }



}

