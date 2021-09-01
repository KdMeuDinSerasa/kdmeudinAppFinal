package com.example.kdmeudinheiro.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kdmeudinheiro.model.UserModel
import com.example.kdmeudinheiro.repository.UserRepository
import com.google.firebase.auth.FirebaseUser

class MainViewModel() : ViewModel() {

    private var _mUserModel = MutableLiveData<UserModel>()
    val mUserModel: LiveData<UserModel> = _mUserModel

    private var _mFirebaseUser = MutableLiveData<FirebaseUser?>()
    val mFirebaseUser: LiveData<FirebaseUser?> = _mFirebaseUser

    private val mUserRepository = UserRepository()

    fun logoutUser(){
        mUserRepository.logOut()
    }

    fun userLoged(){
        mUserRepository.getSession().apply {
            _mFirebaseUser.value = this
        }
    }


    fun getUserById(id: String){
        mUserRepository.getUserById(id){
            _mUserModel.value = it
        }
    }




}