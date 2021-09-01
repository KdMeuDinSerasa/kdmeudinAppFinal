package com.example.kdmeudinheiro.repository

import com.example.kdmeudinheiro.enums.KeysDatabaseBills
import com.example.kdmeudinheiro.model.BillsModel
import com.google.firebase.firestore.FirebaseFirestore

class BillsRepository {
    private val db = FirebaseFirestore.getInstance()

    fun addBills(mBillsModel: BillsModel, callback: (Boolean) -> Unit){
        val map = mutableMapOf<String , String>()
        map.put(KeysDatabaseBills.IDBILL.key, mBillsModel.id_bill.toString())
        mBillsModel.id_user?.let { map.put(KeysDatabaseBills.IDUSER.key, it) }
        map.put(KeysDatabaseBills.PRICE.key, mBillsModel.price)
        map.put(KeysDatabaseBills.TYPEBILL.key, mBillsModel.type_bill)
        map.put(KeysDatabaseBills.NAMEBILL.key, mBillsModel.name_bill)
        map.put(KeysDatabaseBills.EXPIREDATE.key, mBillsModel.expire_date)

        db.collection("table_account").add(map)
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    fun editBill(mBillsModel: BillsModel ,callback: (Boolean) -> Unit){
        val map = mutableMapOf<String, String>()
        map.put(KeysDatabaseBills.PRICE.key, mBillsModel.price)
        map.put(KeysDatabaseBills.TYPEBILL.key, mBillsModel.type_bill)
        map.put(KeysDatabaseBills.NAMEBILL.key, mBillsModel.name_bill)
        map.put(KeysDatabaseBills.EXPIREDATE.key, mBillsModel.expire_date)
        mBillsModel.id_bill?.let { db.collection("table_account").document(it).update(map as Map<String, Any>)
            .addOnSuccessListener { callback(true)
            }
            .addOnFailureListener { callback(false)
            }
        }

    }

    fun deleteBill(mBillsModel: BillsModel, callback: (Boolean) -> Unit){
        mBillsModel.id_bill?.let { db.collection("table_account").document(it).delete()
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
        }
    }


    fun getBills(idUser: String, callback: (List<BillsModel>?, String?) -> Unit){
        db.collection("table_account").whereEqualTo(KeysDatabaseBills.IDUSER.key, idUser).get()
            .addOnSuccessListener {
                val accountList = mutableListOf<BillsModel>()
                it.forEach {
                    accountList.add(BillsModel(
                        it.id,
                        it.data["key_user"] as String,
                        it.data["key_price"] as String,
                        it.data["key_type"] as String,
                        it.data["key_name"] as String,
                        it.data["key_expiredate"] as String
                    ))
                    callback(accountList, null)
                }
            }
            .addOnFailureListener {
                callback(null, it.message)
            }
    }


}