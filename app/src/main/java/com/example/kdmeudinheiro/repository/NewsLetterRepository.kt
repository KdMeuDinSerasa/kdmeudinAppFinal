package com.example.kdmeudinheiro.repository

import com.example.kdmeudinheiro.model.ReciviedArticles
import com.example.kdmeudinheiro.services.NewsLetterService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class NewsLetterRepository @Inject constructor(private val services: NewsLetterService) {

    suspend fun getNews(page: Int): ReciviedArticles? {
        return withContext(Dispatchers.Default) {
            val resp = services.callAPINewsLetter(page = page)
            treatResponse(resp)
        }
    }

    private fun <T> treatResponse(response: Response<T>): T? {
        return if (response.code() == 200) response.body()
        else null
    }
}