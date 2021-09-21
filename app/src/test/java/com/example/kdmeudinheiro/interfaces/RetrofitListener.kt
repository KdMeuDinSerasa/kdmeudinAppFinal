package com.example.kdmeudinheiro.interfaces

import com.example.kdmeudinheiro.model.ReciviedArticles

interface RetrofitListener {
    fun callSuccess()
    fun callFailure()
}