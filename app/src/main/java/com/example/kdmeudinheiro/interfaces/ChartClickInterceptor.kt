package com.example.kdmeudinheiro.interfaces

import com.example.kdmeudinheiro.model.Articles

interface ChartClickInterceptor {
    fun interceptClick(index: Int)
    fun interceptSelectedArticle(article: Articles)
}