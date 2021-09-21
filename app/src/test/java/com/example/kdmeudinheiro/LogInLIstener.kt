package com.example.kdmeudinheiro

interface LogInLIstener {
    fun logInSuccess(email: String?, password: String?)
    fun logInFailure(email: String?, password: String?)

}
