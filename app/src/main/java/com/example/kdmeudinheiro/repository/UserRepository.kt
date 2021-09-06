package com.example.kdmeudinheiro.repository

import com.example.kdmeudinheiro.enums.KeysDatabaseUser
import com.example.kdmeudinheiro.model.UserModel
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
    fun addUser(mUserModel: UserModel, callback: (Boolean) -> Unit){
        val map = mutableMapOf<String , String>()
        map.put(KeysDatabaseUser.EMAIL.key, mUserModel.email)
        map.put(KeysDatabaseUser.NAME.key, mUserModel.name)
        map.put(KeysDatabaseUser.USERID.key, mUserModel.id)

        db.collection("table_user").add(map)
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    fun getUserById(idUser: String, callback: (UserModel?) -> Unit){
        db.collection("table_user").whereEqualTo(KeysDatabaseUser.USERID.key, idUser).get()
            .addOnFailureListener {

            }
            .addOnSuccessListener {
                it.forEach {
                    callback(
                        UserModel(
                            it.id,
                            it.data["user_email"] as String,
                            "",
                            it.data["user_name"] as String
                        )
                    )
                }
            }
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
    //a
    fun getSession(): FirebaseUser? = UserControler.currentUser


    fun logOut(){
        UserControler.signOut()
    }

    fun editUser(mUserModel: UserModel, callback: (Boolean) -> Unit){
        val map = mutableMapOf<String , String>()
        map.put(KeysDatabaseUser.EMAIL.key, mUserModel.email)
        map.put(KeysDatabaseUser.NAME.key, mUserModel.name)
        db.collection("table_user").document(mUserModel.id).update(map as Map<String, Any>)
            .addOnSuccessListener {
                callback(true)
            }
            .addOnSuccessListener {
                callback(false)
            }
    }

}