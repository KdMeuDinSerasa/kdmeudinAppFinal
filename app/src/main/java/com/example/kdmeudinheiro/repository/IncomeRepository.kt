package com.example.kdmeudinheiro.repository

import com.example.kdmeudinheiro.enums.KeysDatabaseIncome
import com.example.kdmeudinheiro.model.IncomeModel
import com.example.kdmeudinheiro.utils.await
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class IncomeRepository @Inject constructor(
    private val db: FirebaseFirestore
) {


    suspend fun addIncome(mIncomeModel: IncomeModel): Boolean {
        val map = mutableMapOf<String, String>()
        map.put(KeysDatabaseIncome.IDUSER.key, mIncomeModel.userId)
        map.put(KeysDatabaseIncome.INCOME.key, mIncomeModel.income)
        val task = db.collection("table_income").add(map)
        task.await()
        return true
    }


    suspend fun editIncome(mIncomeModel: IncomeModel): Boolean {
        val map = mutableMapOf<String, Any>()
        map.put(KeysDatabaseIncome.INCOME.key, mIncomeModel.income)
        val task = db.collection("table_income")
            .document(mIncomeModel.id!!).update(map)

        task.await()

        return true
    }

    suspend fun getIncome(userID: String): IncomeModel? {
        val task = db.collection("table_income")
            .whereEqualTo(KeysDatabaseIncome.IDUSER.key, userID)
            .get()

        val snapshot = task.await()
        var incomeModel: IncomeModel? = null
        snapshot?.forEach {
            incomeModel = IncomeModel(
                it.id,
                it.data[KeysDatabaseIncome.INCOME.key] as String,
                it.data[KeysDatabaseIncome.IDUSER.key] as String
            )
        }
        return incomeModel
    }

}