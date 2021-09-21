package com.example.kdmeudinheiro.viewModel

import com.example.kdmeudinheiro.model.ReciviedArticles

interface RetrofitListener {
    fun callSuccess()
    fun callFailure()
}