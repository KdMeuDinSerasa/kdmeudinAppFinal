package com.example.kdmeudinheiro.model

data class BillsModel(
    val id_bill: Int,
    val id_user: String,
    val price: Double,
    val type_bill: String,
    val name_bill: String,
    val expire_date: String
)