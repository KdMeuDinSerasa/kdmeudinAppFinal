package com.example.kdmeudinheiro.repository

import com.example.kdmeudinheiro.enums.KeysDatabaseIncome
import com.example.kdmeudinheiro.model.IncomeModel
import com.example.kdmeudinheiro.utils.await
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.squareup.okhttp.Dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    suspend fun editIncome2(mIncomeModel: IncomeModel): Boolean{
        val map = mutableMapOf<String, Any>()
        map.put(KeysDatabaseIncome.INCOME.key, mIncomeModel.income)
        val task = db.collection("table_income")
                .document(mIncomeModel.id!!).update(map)

        task.await()

        return true
    }

    suspend fun getIncome(userID: String): IncomeModel? {
        val task =  db.collection("table_income")
            .whereEqualTo(KeysDatabaseIncome.IDUSER.key, userID)
            .get()

        val snapshot = task.await()
        var incomeModel: IncomeModel? = null
        snapshot?.forEach {
            incomeModel = IncomeModel(it.id,
                it.data[KeysDatabaseIncome.INCOME.key] as String,
                it.data[KeysDatabaseIncome.IDUSER.key] as String)
        }
        return incomeModel
    }

}