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
    private val presentDate = calendar.time


    fun checkExpired(): Boolean{
        if (expire_date.before(calendar.time) && status == 0) return true
        return false
    }

    fun checkToExpire(): Boolean{
        calendar.add(Calendar.DATE, 2)
        val datePlus = calendar.time
        if (expire_date.before(datePlus) && expire_date.after(presentDate)  && status == 0) return true
        return false
    }
}