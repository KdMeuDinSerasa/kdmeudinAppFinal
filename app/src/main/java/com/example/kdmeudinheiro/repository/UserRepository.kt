package com.example.kdmeudinheiro.repository

import com.example.kdmeudinheiro.enums.KeysDatabaseUser
import com.example.kdmeudinheiro.model.UserModel
import com.example.kdmeudinheiro.utils.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository {
    private val UserControler = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()


    fun createUserWithEmailPassword(email: String, password: String, callback: (FirebaseUser?, String?) -> Unit) {
        UserControler.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                callback(it.user, null)
            }

            .addOnFailureListener {
                callback(null, it.message)
            }
    }

    suspend fun addUser(mUserModel: UserModel): Boolean{
        val map = mutableMapOf<String , String>()
        map.put(KeysDatabaseUser.EMAIL.key, mUserModel.email)
        map.put(KeysDatabaseUser.NAME.key, mUserModel.name)
        map.put(KeysDatabaseUser.USERID.key, mUserModel.id)

        val task = db.collection("table_user").add(map)
        task.await()
        return true
    }

    suspend fun getUserById(idUser: String): UserModel?{
        var mUserModel: UserModel? = null
        val task = db.collection("table_user").whereEqualTo(KeysDatabaseUser.USERID.key, idUser).get()
        val result = task.await()
        result?.forEach {
            mUserModel = UserModel(
                it.id,
                it.data["user_email"] as String,
                "",
                it.data["user_name"] as String
            )
        }
        return mUserModel

    }



    fun loginWithEmailPassword(email: String, password: String , callback: (FirebaseUser?, String?) -> Unit) {
        UserControler.signInWithEmailAndPassword(email , password)
            .addOnSuccessListener {
                callback(it.user , null)
            }
            .addOnFailureListener {
                callback(null , it.message)
            }
    }

    fun getSession(): FirebaseUser? = UserControler.currentUser


    fun logOut(){
        UserControler.signOut()
    }

    suspend fun editUser(mUserModel: UserModel): Boolean{
        val map = mutableMapOf<String , Any>()
        map.put(KeysDatabaseUser.EMAIL.key, mUserModel.email)
        map.put(KeysDatabaseUser.NAME.key, mUserModel.name)
        val task = db.collection("table_user").document(mUserModel.id).update(map)
        task.await()
        return true
    }


}