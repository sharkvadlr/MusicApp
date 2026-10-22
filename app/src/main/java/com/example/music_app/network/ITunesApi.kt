package com.example.music_app.network

import com.example.music_app.model.ITunesResponse
import retrofit2.http.GET
import retrofit2.http.Query

// ARCHIVOS PARA LA CONSULTA DE INFORMACIÓN (LOCAL O INTERNET)

interface ITunesApi {

    // Indicar método HTTP a usar
    @GET("search")

    // Método para la obtener la información con la URL del API
    fun obtenerAlbumGenero(
        @Query("term") genero: String,
        @Query("contry") pais: String,
        @Query("media") media: String,
        @Query("entity") entidad: String,
        @Query("limit") limite: Int,
    ) : retrofit2.Call<ITunesResponse>




}