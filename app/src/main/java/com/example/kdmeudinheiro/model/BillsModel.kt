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
){
    private val calendar = Calendar.getInstance()


    fun checkExpired(): Boolean{
        if (expire_date.after(calendar.time) && status == 0) return true
        return false
    }

    fun checkToExpire(): Boolean{
        calendar.add(Calendar.DATE, 1)
        val datePlus = calendar.time
        if (expire_date.before(datePlus) && status == 0) return true
        return false
    }
}