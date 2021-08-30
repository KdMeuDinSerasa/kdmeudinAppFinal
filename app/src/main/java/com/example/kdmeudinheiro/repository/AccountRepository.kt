package com.example.kdmeudinheiro.repository

import com.example.kdmeudinheiro.enums.KeysDatabaseAccount
import com.example.kdmeudinheiro.model.AccountModel
import com.google.firebase.firestore.FirebaseFirestore

class AccountRepository {
    private val db = FirebaseFirestore.getInstance()

    fun addAccount(mAccountModel: AccountModel, callback: (Boolean) -> Unit){
        val map = mutableMapOf<String , String>()
        map.put(KeysDatabaseAccount.IDACCOUNT.key, mAccountModel.id_account.toString())
        map.put(KeysDatabaseAccount.IDUSER.key, mAccountModel.id_user)
        map.put(KeysDatabaseAccount.PRICE.key, mAccountModel.price.toString())
        map.put(KeysDatabaseAccount.TYPEACCOUNT.key, mAccountModel.type_account)
        map.put(KeysDatabaseAccount.NAMEACCOUNT.key, mAccountModel.name_account)
        map.put(KeysDatabaseAccount.EXPIREDATE.key, mAccountModel.expire_date)

        db.collection("table_account").add(map)
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }


}