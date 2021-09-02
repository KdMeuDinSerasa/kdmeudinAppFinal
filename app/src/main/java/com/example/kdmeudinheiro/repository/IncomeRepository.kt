package com.example.kdmeudinheiro.repository

import com.example.kdmeudinheiro.enums.KeysDatabaseIncome
import com.example.kdmeudinheiro.model.IncomeModel
import com.google.firebase.firestore.FirebaseFirestore

class IncomeRepository {
    private val db = FirebaseFirestore.getInstance()

    fun addIncome(mIncomeModel: IncomeModel, callback: (Boolean) -> Unit) {
        val map = mutableMapOf<String, String>()
        map.put(KeysDatabaseIncome.IDUSER.key, mIncomeModel.userId)
        map.put(KeysDatabaseIncome.INCOME.key, mIncomeModel.income)
        db.collection("table_income").add(map)
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    fun editIncome(mIncomeModel: IncomeModel, callback: (Boolean) -> Unit) {
        val map = mutableMapOf<String, String>()
        map.put(KeysDatabaseIncome.INCOME.key, mIncomeModel.income)
        mIncomeModel.id?.let {
            db.collection("table_income").document(it).update(map as Map<String, Any>)
                .addOnSuccessListener {
                    callback(true)
                }
                .addOnFailureListener {
                    callback(false)
                }

        }
    }

    fun getIncome(userID: String, callback: (IncomeModel?, String?) -> Unit) {
        db.collection("table_income").whereEqualTo(KeysDatabaseIncome.IDUSER.key, userID).get()
            .addOnSuccessListener {
                it.forEach {
                    callback(
                        IncomeModel(
                            it.id,
                            it.data[KeysDatabaseIncome.INCOME.key] as String,
                            it.data[KeysDatabaseIncome.IDUSER.key] as String
                        ), null
                    )
                }
            }
            .addOnFailureListener {
                callback(null , it.message)
            }
    }

}