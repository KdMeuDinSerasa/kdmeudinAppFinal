package com.example.kdmeudinheiro.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kdmeudinheiro.model.IncomeModel
import com.example.kdmeudinheiro.model.UserModel
import com.example.kdmeudinheiro.repository.IncomeRepository
import com.example.kdmeudinheiro.repository.UserRepository
import com.google.firebase.auth.FirebaseUser

class MainViewModel() : ViewModel() {

    private var _mUserModel = MutableLiveData<UserModel>()
    val mUserModel: LiveData<UserModel> = _mUserModel

    private var _mFirebaseUser = MutableLiveData<FirebaseUser?>()
    val mFirebaseUser: LiveData<FirebaseUser?> = _mFirebaseUser

    private var _mIncomeModel = MutableLiveData<IncomeModel?>()
    val mIncomeModel: LiveData<IncomeModel?> = _mIncomeModel

    private var _mError = MutableLiveData<String?>()
    val mError: LiveData<String?> = _mError

    private val mUserRepository = UserRepository()
    private val mIncomeRepository = IncomeRepository()

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

    fun getIncome(userId: String){
        mIncomeRepository.getIncome(userId){incomeModel,error ->
            if (incomeModel != null) _mIncomeModel.value = incomeModel
            if (error != null) _mError.value = error
        }
    }
    fun editIncome(mIncomeModel: IncomeModel){
        mIncomeRepository.editIncome(mIncomeModel){
            if (!it) _mError.value = "Erro ao editar"
        }
    }

    fun addIncome(mIncomeModel: IncomeModel){
        mIncomeRepository.addIncome(mIncomeModel){
            if (!it) _mError.value = "Erro ao adicionar"

        }
    }




}