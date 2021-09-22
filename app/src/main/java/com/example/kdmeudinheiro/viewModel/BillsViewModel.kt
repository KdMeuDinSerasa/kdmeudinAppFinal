package com.example.kdmeudinheiro.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kdmeudinheiro.model.BillsModel
import com.example.kdmeudinheiro.repository.BillsRepository
import com.example.kdmeudinheiro.repository.UserRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class BillsViewModel @Inject constructor(
    private val billRepo: BillsRepository,
    private val userRepo: UserRepository
): ViewModel() {

    private val _billList = MutableLiveData<List<BillsModel>>()
    var billList: LiveData<List<BillsModel>> = _billList

    private val _error = MutableLiveData<String>()
    var error: LiveData<String> = _error

    fun getAllBills(idUser: String) {
        viewModelScope.launch {
            val listBills = billRepo.getBills(idUser)
            if (listBills != null)
                _billList.value = listBills!!
            else
                _error.value = "Adicione Suas Constas"
        }
    }

    private var _copyBillList = MutableLiveData<List<BillsModel>>()
    var copyBillList: LiveData<List<BillsModel>> = _copyBillList

    private val _addResponse = MutableLiveData<Boolean>()
    var addResponse: LiveData<Boolean> = _addResponse

    fun addBill(bill: BillsModel) {
        viewModelScope.launch {
            _addResponse.value = billRepo.addBills(bill)
        }
    }

    private val _editResponse = MutableLiveData<Boolean>()
    var editResponse: LiveData<Boolean> = _editResponse

    fun editBill(bill: BillsModel) {
        viewModelScope.launch {
            _editResponse.value = billRepo.editBill(bill)
        }
    }

    private val _deleteResponse = MutableLiveData<Boolean>()
    var deleteResponse: LiveData<Boolean> = _deleteResponse

    fun deleteBill(bill: BillsModel) {
        viewModelScope.launch {
            _deleteResponse.value = billRepo.deleteBill(bill)
        }
    }


    fun filterPay(date: Date, getUserChoice: Int) {

        var filtered = _billList.value

        viewModelScope.launch {

            if (getUserChoice == 0)
                filtered = filtered?.filter {
                    it.expire_date.after(date) && it.status == 0
                }
            else if (getUserChoice == 1)
                filtered = filtered?.filter {
                    it.expire_date.before(date) && it.status == 0
                }
            else if (getUserChoice == 2)
                filtered = filtered?.filter {
                    it.status == 1
                }

            _copyBillList.value = filtered!!
        }
    }

    fun filterBill(filter: String) {
        viewModelScope.launch {
            if (filter.isNullOrEmpty())
                _copyBillList.value = _billList.value
            val filtered = _copyBillList.value?.filter {
                it.name_bill.contains(filter)
            }
            _copyBillList.value = filtered!!
        }

    }
}
