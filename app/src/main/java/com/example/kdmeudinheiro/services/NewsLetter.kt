package com.example.kdmeudinheiro.services

import com.example.kdmeudinheiro.enums.APIKEY
import com.google.android.gms.common.api.internal.ApiKey
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsLetter {

    @GET("top-headlines?")
    suspend fun callAPI(
        @Query("apiKey") key: String = APIKEY.TOKEN.key,
        @Query("country") country: String = "br",
        @Query("category") category: String = "business",
    ): Response<??>

}