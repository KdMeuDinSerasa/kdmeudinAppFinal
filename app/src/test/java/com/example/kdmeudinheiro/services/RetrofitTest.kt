package com.example.kdmeudinheiro.services

import com.example.kdmeudinheiro.enums.APIKEY
import com.example.kdmeudinheiro.model.NewsLetter
import com.example.kdmeudinheiro.model.ReciviedArticles
import com.example.kdmeudinheiro.model.Source
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RetrofitTest {

    val source = Source("tireido")
    val expectedResponse = listOf(
        NewsLetter(
            "Artigo 1",
            "este é um artigo",
            "urlDoArtigo.com",
            "uma imagem",
            "21/09/2021",
            source
        ),
        NewsLetter(
            "Artigo 2",
            "este é um artigo",
            "urlDoArtigo.com",
            "uma imagem",
            "21/09/2021",
            source
        )
    )
    val mockResp = ReciviedArticles(expectedResponse)
    val expectedEmptyResp = emptyList<NewsLetter>()


    @Mock
    lateinit var service: NewsLetterService

    @Before
    internal  fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    internal suspend fun should_do_returns_list_of_news() {
        Mockito.`when`(service.callAPINewsLetter(APIKEY.TOKEN.key).body()).thenReturn(mockResp)
        val actualResponse = service.callAPINewsLetter().body()
        assertThat(actualResponse).isEqualTo(expectedResponse)
    }


}