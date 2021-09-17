package com.example.kdmeudinheiro.services

import com.google.android.gms.common.api.internal.ApiKey
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsLetter {

    @GET("top-headlines?")
    suspend fun callAPI(
        @Query("apiKey") key: String = ,
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("lang") lang: String = "pt"
    ): Response<??>

}