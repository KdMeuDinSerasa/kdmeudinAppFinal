package com.example.kdmeudinheiro.repository

import android.net.Uri
import com.example.kdmeudinheiro.enums.KeysDatabaseUser
import com.example.kdmeudinheiro.model.UserModel
import com.example.kdmeudinheiro.utils.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class UserRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val mUserControler: FirebaseAuth,
    private val mFirestorage: FirebaseStorage
) {
    private val reference = mFirestorage.reference


    fun uploadImgToFirebase(img: String, imgUri: Uri, callback: (Uri?, String?) -> Unit) {
        val fileRef = reference.child("${System.currentTimeMillis()}.$img")
        fileRef.putFile(imgUri)
            .addOnSuccessListener {
                fileRef.downloadUrl.addOnSuccessListener {
                    callback(it, null)
                }
                    .addOnFailureListener {
                        callback(null, it.message)
                    }
            }

            .addOnFailureListener {
                callback(null, it.message)
            }


    }


    fun createUserWithEmailPassword(
        email: String,
        password: String,
        callback: (FirebaseUser?, String?) -> Unit
    ) {
        mUserControler.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                callback(it.user, null)
            }

            .addOnFailureListener {
                callback(null, it.message)
            }
    }

    suspend fun ResetUserPassword(email: String): Boolean{
        val task = mUserControler.sendPasswordResetEmail(email)
        task.await()
        return true

    }

    suspend fun addUser(mUserModel: UserModel): Boolean {
        val map = mutableMapOf<String, String>()
        map.put(KeysDatabaseUser.EMAIL.key, mUserModel.email)
        map.put(KeysDatabaseUser.NAME.key, mUserModel.name)
        map.put(KeysDatabaseUser.USERID.key, mUserModel.id)

        val task = db.collection("table_user").add(map)
        task.await()
        return true
    }

    suspend fun getUserById(idUser: String): UserModel? {
        var mUserModel: UserModel? = null
        val task =
            db.collection("table_user").whereEqualTo(KeysDatabaseUser.USERID.key, idUser).get()
        val result = task.await()
        result?.forEach {
            mUserModel = UserModel(
                it.id,
                it.data["user_email"] as String,
                "",
                it.data["user_name"] as String,
                it.data["user_avatar"] as? String?
            )
        }
        return mUserModel

    }


    fun loginWithEmailPassword(
        email: String,
        password: String,
        callback: (FirebaseUser?, String?) -> Unit
    ) {
        mUserControler.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                callback(it.user, null)
            }
            .addOnFailureListener {
                callback(null, it.message)
            }
    }

    fun getSession(): FirebaseUser? = mUserControler.currentUser


    fun logOut() {
        mUserControler.signOut()
    }

    suspend fun editUser(mUserModel: UserModel): Boolean {
        val map = mutableMapOf<String, Any>()
        map.put(KeysDatabaseUser.EMAIL.key, mUserModel.email)
        map.put(KeysDatabaseUser.NAME.key, mUserModel.name)
        mUserModel.img?.let { map.put(KeysDatabaseUser.IMGUSER.key, it) }
        val task = db.collection("table_user").document(mUserModel.id).update(map)
        task.await()
        return true
    }


}