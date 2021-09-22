package com.example.kdmeudinheiro.model

import com.example.kdmeudinheiro.utils.isValidEmail

data class UserModel(
    val id: String,
    var email: String,
    val password: String,
    var name: String,
    var img: String?
) {
    constructor(email: String, password: String, name: String) : this("", email, password, name, null)


    fun checkInsertData(): Boolean{
        return ((!email.isNullOrEmpty() && !password.isNullOrEmpty() && !name.isNullOrEmpty()) && (password.length >= 6 && email.contains('@') && email.contains('.') && name.length >= 4))
    }
}
