package com.example.practicadiseo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface tesisAPI {

    @GET("api/UsuarioAPI")
    Call<Usuario> getUsuario(@Query("id") String id,
                                   @Query("pass") String pass
    );
    @POST("api/UsuarioAPI")
    Call<Usuario> registerUsuario(@Query("id") String id,
                             @Query("pass") String pass
    );


    @GET("api/CiudadAPI/")
    Call<List<Ciudad>> getCiudades();



    @GET("api/SolicitudAPI/")
    Call<List<Solicitud>> getSolicitudes(@Query("rut") String rut

    );
}
