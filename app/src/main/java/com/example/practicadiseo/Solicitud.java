package com.example.practicadiseo;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;
import java.util.ArrayList;

public class Solicitud implements Parcelable {

    private int idSolicitud;
    private String Fecha;
    private String Descripcion;
    private int IdFoto;
    private int idEstadoSolicitud;
    private String Rut_Cliente;
    private String Rut_Trabajador;

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
        idEstadoSolicitud=in.readInt();
        IdFoto=in.readInt();
        Rut_Cliente= in.readString();
        Rut_Trabajador= in.readString();
        in.readTypedList(lista,CREATOR);
    }
    public void writeToParcel(Parcel dest, int flags){
        dest.writeInt(idSolicitud);
        dest.writeString(Fecha);
        dest.writeString(Descripcion);
        dest.writeInt(idEstadoSolicitud);
        dest.writeInt(IdFoto);
        dest.writeString(Rut_Cliente);
        dest.writeString(Rut_Trabajador);
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

    public Solicitud(int idSolicitud, String fecha, String descripcion, int idFoto,int idEstadoSolicitud, String rut_Cliente, String rut_Trabajador) {
        this.idSolicitud = idSolicitud;
        this.Fecha = fecha;
        this.Descripcion = descripcion;
        this.IdFoto = idFoto;
        this.idEstadoSolicitud = idEstadoSolicitud;
        this.Rut_Cliente = rut_Cliente;
        this.Rut_Trabajador = rut_Trabajador;
    }

    public Solicitud(Solicitud solicitud) {
        this.idSolicitud = solicitud.idSolicitud;
        this.Fecha = solicitud.Fecha;
        this.Descripcion = solicitud.Descripcion;
        this.idEstadoSolicitud = solicitud.idEstadoSolicitud;
        this.IdFoto = solicitud.IdFoto;
        this.Rut_Trabajador = solicitud.Rut_Trabajador;
        this.Rut_Cliente = solicitud.Rut_Cliente;
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

    public int getIdFoto() {
        return IdFoto;
    }

    public void setIdFoto(int idFoto) {
        IdFoto = idFoto;
    }

    public int getIdEstadoSolicitud() {
        return idEstadoSolicitud;
    }

    public void setIdEstadoSolicitud(int idEstadoSolicitud) {
        this.idEstadoSolicitud = idEstadoSolicitud;
    }

    public String getRut_Cliente() {
        return Rut_Cliente;
    }

    public void setRut_Cliente(String rut_Cliente) {
        Rut_Cliente = rut_Cliente;
    }

    public String getRut_Trabajador() {
        return Rut_Trabajador;
    }

    public void setRut_Trabajador(String rut_Trabajador) {
        Rut_Trabajador = rut_Trabajador;
    }

    public ArrayList<Solicitud> getLista() {
        return lista;
    }

    public void setLista(ArrayList<Solicitud> lista) {
        this.lista = lista;
    }

    public static Creator<Solicitud> getCREATOR() {
        return CREATOR;
    }
}

