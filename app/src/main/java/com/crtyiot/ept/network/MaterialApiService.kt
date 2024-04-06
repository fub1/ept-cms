package com.crtyiot.ept.network

import com.crtyiot.ept.data.model.Material
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://material.viget.net"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface MaterialApiService {
    @GET("/materials")
    suspend fun getMaterials(): List<Material>

    companion object {
        fun create(): MaterialApiService {
            return retrofit.create(MaterialApiService::class.java)
        }
    }
}