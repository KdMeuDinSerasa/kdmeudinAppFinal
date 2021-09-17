package com.example.kdmeudinheiro.repository

import android.widget.DatePicker
import com.example.kdmeudinheiro.enums.KeysDatabaseBills
import com.example.kdmeudinheiro.model.BillsModel
import com.example.kdmeudinheiro.utils.await
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.type.Date
import java.util.*
import javax.inject.Inject

class BillsRepository @Inject constructor(
    private val db: FirebaseFirestore
) {




    suspend fun addBills(mBillsModel: BillsModel): Boolean {
        val map = mutableMapOf<String, Any>()
        map.put(KeysDatabaseBills.IDBILL.key, mBillsModel.id_bill.toString())
        mBillsModel.id_user?.let { map.put(KeysDatabaseBills.IDUSER.key, it) }
        map.put(KeysDatabaseBills.PRICE.key, mBillsModel.price)
        map.put(KeysDatabaseBills.TYPEBILL.key, mBillsModel.type_bill)
        map.put(KeysDatabaseBills.NAMEBILL.key, mBillsModel.name_bill)
        map.put(KeysDatabaseBills.EXPIREDATE.key, mBillsModel.expire_date)
        map.put(KeysDatabaseBills.STATUS.key, mBillsModel.status.toString())

        val taks = db.collection("table_account").add(map)
        taks.await()
        return true
    }



    suspend fun editBill(mBillsModel: BillsModel): Boolean{
        val map = mutableMapOf<String, Any>()
        map.put(KeysDatabaseBills.PRICE.key, mBillsModel.price)
        map.put(KeysDatabaseBills.TYPEBILL.key, mBillsModel.type_bill)
        map.put(KeysDatabaseBills.NAMEBILL.key, mBillsModel.name_bill)
        map.put(KeysDatabaseBills.EXPIREDATE.key, mBillsModel.expire_date)
        map.put(KeysDatabaseBills.STATUS.key, mBillsModel.status.toString())
        val task = mBillsModel.id_bill?.let { db.collection("table_account").document(it).update(map) }
        if (task != null) {
            task.await()
        }
        return true
    }


    suspend fun deleteBill(mBillsModel: BillsModel): Boolean{
        val task = mBillsModel.id_bill?.let { db.collection("table_account").document(it).delete() }
        if (task != null) {
            task.await()
        }
        return true
    }



    suspend fun getBills(idUser: String): List<BillsModel>?{
        val task = db.collection("table_account").whereEqualTo(KeysDatabaseBills.IDUSER.key, idUser).get()
        val result = task.await()
        val accountList = mutableListOf<BillsModel>()
        result?.forEach {
            val dateFromFB = it.data["key_expiredate"] as Timestamp
            accountList.add(
                BillsModel(
                    it.id,
                    it.data["key_user"] as String,
                    it.data["key_price"] as String,
                    it.data["key_type"] as String,
                    it.data["key_name"] as String,
                    dateFromFB.toDate(),
                    (it.data["key_status_paid"] as? String?)?.toInt()
                )
            )
        }
        return accountList
    }


}