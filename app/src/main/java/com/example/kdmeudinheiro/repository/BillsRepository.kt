package com.example.kdmeudinheiro.repository

import com.example.kdmeudinheiro.enums.KeysDatabaseAccount
import com.example.kdmeudinheiro.model.BillsModel
import com.google.firebase.firestore.FirebaseFirestore

class BillsRepository {
    private val db = FirebaseFirestore.getInstance()

    fun addAccount(mBillsModel: BillsModel, callback: (Boolean) -> Unit){
        val map = mutableMapOf<String , String>()
        map.put(KeysDatabaseAccount.IDACCOUNT.key, mBillsModel.id_account.toString())
        map.put(KeysDatabaseAccount.IDUSER.key, mBillsModel.id_user)
        map.put(KeysDatabaseAccount.PRICE.key, mBillsModel.price.toString())
        map.put(KeysDatabaseAccount.TYPEACCOUNT.key, mBillsModel.type_account)
        map.put(KeysDatabaseAccount.NAMEACCOUNT.key, mBillsModel.name_account)
        map.put(KeysDatabaseAccount.EXPIREDATE.key, mBillsModel.expire_date)

        db.collection("table_account").add(map)
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    fun getAccounts(idUser: String, callback: (List<BillsModel>?, String?) -> Unit){
        db.collection("table_account").whereEqualTo(KeysDatabaseAccount.IDUSER.key, idUser).get()
            .addOnSuccessListener {
                val accountList = mutableListOf<BillsModel>()
                it.forEach {
                    accountList.add(BillsModel(
                        it.id.toInt(),
                        it.data["key_user"] as String,
                        it.data["key_price"] as Double,
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