package com.aritra.goldmannasa.data.remote.network

import com.aritra.goldmannasa.BuildConfig
import com.aritra.goldmannasa.data.remote.dtos.APODDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaApi {
    @GET("/planetary/apod")
    suspend fun getLatestAPOD(
        @Query("api_key") api_key: String = BuildConfig.API_KEY,
    ): Response<APODDto>

    @GET("/planetary/apod")
    suspend fun getDatedAPOD(
        @Query("api_key") api_key: String = BuildConfig.API_KEY,
        @Query("date") date: String,
    ): Response<APODDto>
}