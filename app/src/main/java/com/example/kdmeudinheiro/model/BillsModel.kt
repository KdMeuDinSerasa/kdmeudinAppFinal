package com.example.kdmeudinheiro.model

import java.util.*


data class BillsModel(
    val id_bill: String?,
    var id_user: String?,
    val price: String,
    val type_bill: String,
    val name_bill: String,
    val expire_date: Date,
    var status: Int?
)