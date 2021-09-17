package com.example.kdmeudinheiro.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kdmeudinheiro.enums.TypesOfBills
import com.example.kdmeudinheiro.model.Articles
import com.example.kdmeudinheiro.model.BillsModel
import com.example.kdmeudinheiro.model.IncomeModel
import com.example.kdmeudinheiro.model.UserModel
import com.example.kdmeudinheiro.repository.ArticlesRepository
import com.example.kdmeudinheiro.repository.BillsRepository
import com.example.kdmeudinheiro.repository.IncomeRepository
import com.example.kdmeudinheiro.repository.UserRepository
import com.github.mikephil.charting.data.Entry
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val mUserRepository: UserRepository,
    private val mIncomeRepository: IncomeRepository,
    private val mBillsRepository: BillsRepository,
    private val mArticlesRepository: ArticlesRepository
) : ViewModel() {

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

    private var _billsPercentage = MutableLiveData<List<BillsModel>>()
    val billsPercentage: LiveData<List<BillsModel>> = _billsPercentage

    private var _articlesList = MutableLiveData<List<Articles>>()
    val articlesList: LiveData<List<Articles>> = _articlesList


    fun logoutUser() {
        mUserRepository.logOut()
    }

    fun userLoged() {
        mUserRepository.getSession().apply {
            _mFirebaseUser.value = this
        }
    }

    fun getUserById(id: String) {
        viewModelScope.launch {
            _mUserModel.value = mUserRepository.getUserById(id)
            delay(3000)
        }

    }

    fun getIncome(userId: String) {
        viewModelScope.launch {
            _mIncomeModel.value = mIncomeRepository.getIncome(userId)

        }

    }

    fun editIncome(mIncomeModel: IncomeModel) {
        viewModelScope.launch {
            if (!mIncomeRepository.editIncome(mIncomeModel))
                _mError.value = "Erro ao editar"
            userLoged()
        }
    }

    fun addIncome(mIncomeModel: IncomeModel) {
        viewModelScope.launch {
            if (!mIncomeRepository.addIncome(mIncomeModel))
                _mError.value = "Erro ao adicionar"
            userLoged()

        }

    }

    fun getOutcome(userId: String) {
        var outCome = 0.0
        viewModelScope.launch {
            val listBills = mBillsRepository.getBills(userId)
            listBills?.forEach {
                outCome += it.price.toDouble()
            }
            _outCome.value = outCome
            getIncomeAndBills(userId)
        }
    }


    fun getIncomeAndBills(userId: String) {
        viewModelScope.launch {
            var bills = mBillsRepository.getBills(userId)
            var articles = mArticlesRepository.getArticles()
            getBills(bills, articles)
        }
    }

    fun getBills(bills: List<BillsModel>?, articles: List<Articles>) {
        _articlesList.value = articles
        _billsPercentage.value = bills!!

    }


}