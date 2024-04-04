package com.crtyiot.ept.network

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET


private const val BASE_URL = "https://material.viget.net"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface MaterialApiService {
    @GET("/materials")
    suspend fun getMaterials(): String
}

object MatApi {
    val retrofitService : MaterialApiService by lazy {
        retrofit.create(MaterialApiService::class.java)
    }
}