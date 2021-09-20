package com.example.kdmeudinheiro.model

import com.google.gson.annotations.SerializedName

data class ReciviedArticles(

    @SerializedName("articles")
    val news: List<NewsLetter>
)

data class NewsLetter(
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val desc: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("urlToImage")
    val imageUrl: String,
    @SerializedName("publishedAt")
    val date: String,
    @SerializedName("source")
    val source: Source

)

data class Source(
    @SerializedName("name")
    val siteName: String
)