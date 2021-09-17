package com.example.kdmeudinheiro.services

import com.example.kdmeudinheiro.enums.APIKEY
import com.example.kdmeudinheiro.model.NewsLetter
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsLetterService {

    @GET("top-headlines?")
    suspend fun callAPINewsLetter(
        @Query("apiKey") key: String = APIKEY.TOKEN.key,
        @Query("country") country: String = "br",
        @Query("category") category: String = "business",
        @Query("page") page: Int = 1,
    ): Response<NewsLetter>

}