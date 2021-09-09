package com.example.kdmeudinheiro.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kdmeudinheiro.model.BillsModel
import com.example.kdmeudinheiro.model.IncomeModel
import com.example.kdmeudinheiro.model.UserModel
import com.example.kdmeudinheiro.repository.BillsRepository
import com.example.kdmeudinheiro.repository.IncomeRepository
import com.example.kdmeudinheiro.repository.UserRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class MainViewModel() : ViewModel() {

    private var _mUserModel = MutableLiveData<UserModel>()
    val mUserModel: LiveData<UserModel> = _mUserModel

    private var _mFirebaseUser = MutableLiveData<FirebaseUser?>()
    val mFirebaseUser: LiveData<FirebaseUser?> = _mFirebaseUser

    private var _mIncomeModel = MutableLiveData<IncomeModel?>()
    val mIncomeModel: LiveData<IncomeModel?> = _mIncomeModel

    private var _mError = MutableLiveData<String?>()
    val mError: LiveData<String?> = _mError

    private var _outCome = MutableLiveData<Double?>()
    val outCome: LiveData<Double?> = _outCome

    private var _totalBills = MutableLiveData<Int>()
    var totalBills: LiveData<Int> = _totalBills

    private val mUserRepository = UserRepository()
    private val mIncomeRepository = IncomeRepository()
    private val mBillsRepository = BillsRepository()

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
        viewModelScope.launch {
            _mIncomeModel.value = mIncomeRepository.getIncome(userId)
        }

    }
    fun editIncome(mIncomeModel: IncomeModel){
        viewModelScope.launch {
            if (!mIncomeRepository.editIncome2(mIncomeModel)) {
                _mError.value = "Erro ao editar"
            }
            userLoged()
        }
    }

    fun addIncome(mIncomeModel: IncomeModel){
        mIncomeRepository.addIncome(mIncomeModel){
            if (!it) _mError.value = "Erro ao adicionar"

        }
    }
    fun getOutcome(userId: String){
        var outCome = 0.0
        mBillsRepository.getBills(userId){ listBills,errorMesage ->
            listBills?.forEach {
                outCome += it.price.toDouble()

            }
            getTotalOfBills(listBills)
            _outCome.value = outCome
            if (errorMesage != null) _mError.value = errorMesage
        }
    }
    fun getTotalOfBills(bills: List<BillsModel>?){
        bills?.size.let { billsSize ->
            _totalBills.value = billsSize
        }
    }




}