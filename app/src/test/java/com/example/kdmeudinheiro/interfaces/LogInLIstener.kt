package com.example.kdmeudinheiro.interfaces

interface LogInLIstener {
    fun logInSuccess(email: String?, password: String?)
    fun logInFailure(email: String?, password: String?)

}
