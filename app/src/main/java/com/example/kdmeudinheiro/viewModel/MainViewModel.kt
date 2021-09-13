package com.example.kdmeudinheiro.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kdmeudinheiro.enums.TypesOfBills
import com.example.kdmeudinheiro.model.BillsModel
import com.example.kdmeudinheiro.model.IncomeModel
import com.example.kdmeudinheiro.model.UserModel
import com.example.kdmeudinheiro.repository.BillsRepository
import com.example.kdmeudinheiro.repository.IncomeRepository
import com.example.kdmeudinheiro.repository.UserRepository
import com.github.mikephil.charting.data.Entry
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val mUserRepository: UserRepository,
    private val mIncomeRepository: IncomeRepository,
    private val mBillsRepository: BillsRepository
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

    private var _billsPercentage = MutableLiveData<ArrayList<Entry>>()
    val billsPercentage: LiveData<ArrayList<Entry>> = _billsPercentage


    fun logoutUser() {
        mUserRepository.logOut()
    }

    fun userLoged() {
        mUserRepository.getSession().apply {
            _mFirebaseUser.value = this
            getIncomeAndBills(this!!.uid)
        }
    }

    fun getUserById(id: String) {
        viewModelScope.launch {
            _mUserModel.value = mUserRepository.getUserById(id)
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
        mBillsRepository.getBills(userId) { listBills, errorMesage ->
            listBills?.forEach {
                outCome += it.price.toDouble()
            }
            _outCome.value = outCome
        }
    }

    fun getIncomeAndBills(userId: String) {
        viewModelScope.launch {
            var income = mIncomeRepository.getIncome(userId)
            var bills = mBillsRepository.getBills(userId)
            getBills(bills, income)
        }
    }

    fun getBills(bills: List<BillsModel>?, income: IncomeModel?) {
        val pieChartEntry = ArrayList<Entry>()
        bills?.forEach { bills ->
            val mIncome = income?.let { income -> income.income.toDouble() / 100 }
            when (bills.type_bill) {
                TypesOfBills.FIX_BILLS.catName -> pieChartEntry.add(
                    Entry(
                        (((bills.price.toDouble() / 100) - mIncome!!) * 100).toFloat(),
                        0
                    )
                )
                TypesOfBills.LEISURE_BILLS.catName -> pieChartEntry.add(
                    Entry(
                        (((bills.price.toDouble() / 100) - mIncome!!) * 100).toFloat(),
                        1
                    )
                )
                TypesOfBills.MONTHLY_BILLS.catName -> pieChartEntry.add(
                    Entry(
                        (((bills.price.toDouble() / 100) - mIncome!!) * 100).toFloat(),
                        2
                    )
                )
                TypesOfBills.EMERGENCY_BILL.catName -> pieChartEntry.add(
                    Entry(
                        (((bills.price.toDouble() / 100) - mIncome!!) * 100).toFloat(),
                        3
                    )
                )
            }
        }
        _billsPercentage.value = pieChartEntry
    }


}