package com.example.kdmeudinheiro.viewModel

import androidx.lifecycle.ViewModel
import com.example.kdmeudinheiro.repository.UserRepository

class MainViewModel() : ViewModel() {
    private val mUserRepository = UserRepository()
    fun logoutUser(){
        mUserRepository.logOut()
    }




}