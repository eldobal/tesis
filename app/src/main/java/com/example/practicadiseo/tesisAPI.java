package com.example.practicadiseo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface tesisAPI {

    @GET("api/UsuarioAPI/id={id}&pass={pass}")
    Call<List<Usuario>> getUsuario(@Path("id") String id,
                                   @Path("pass") String pass
    );
}
