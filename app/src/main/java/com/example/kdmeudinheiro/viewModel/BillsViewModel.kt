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
import javax.inject.Inject

@HiltViewModel
class BillsViewModel @Inject constructor(
    private val billRepo: BillsRepository,
    private val userRepo: UserRepository
): ViewModel() {


    private val _user = MutableLiveData<FirebaseUser>()
    var user: LiveData<FirebaseUser> = _user

    fun getUserId() {
        userRepo.getSession().apply {
            _user.value = this
        }
    }

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
}
