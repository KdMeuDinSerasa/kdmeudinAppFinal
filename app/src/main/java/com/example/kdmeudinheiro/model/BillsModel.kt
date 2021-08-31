package com.example.kdmeudinheiro.model

data class BillsModel(
    val id_account: Int,
    val id_user: String,
    val price: Double,
    val type_account: String,
    val name_account: String,
    val expire_date: String
)