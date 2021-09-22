package com.example.kdmeudinheiro.model

data class UserModel(
    val id: String,
    var email: String,
    val password: String,
    var name: String,
    var img: String?
) {
    constructor(email: String, password: String, name: String) : this("", email, password, name, null)
    constructor(email: String, password: String): this("", email, password, "", null)


    fun checkInsertData(): Boolean{
        return ((!email.isNullOrEmpty() && !password.isNullOrEmpty() && !name.isNullOrEmpty()) && (password.length >= 6 && email.contains('@') && email.contains('.') && name.length >= 4))
    }

    fun checkLogin(): Boolean {
        return (!email.isNullOrEmpty() && !password.isNullOrBlank() && (password.length >= 6 && email.contains('@') && email.contains('.')) ) }
}
