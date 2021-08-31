package com.example.kdmeudinheiro.repository

import com.example.kdmeudinheiro.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class UserRepository {
    private val UserControler = FirebaseAuth.getInstance()


    fun createUserWithEmailPassword(mUserModel: UserModel, callback: (FirebaseUser?, String?) -> Unit) {
        UserControler.createUserWithEmailAndPassword(mUserModel.email, mUserModel.password)
            .addOnSuccessListener {
                callback(it.user, null)
            }

            .addOnFailureListener {
                callback(null, it.message)
            }
    }

    fun loginWithEmailPassword(mUserModel: UserModel , callback: (FirebaseUser?, String?) -> Unit) {
        UserControler.signInWithEmailAndPassword(mUserModel.email , mUserModel.password)
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

}