package com.example.kdmeudinheiro.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kdmeudinheiro.model.BillsModel
import com.example.kdmeudinheiro.repository.BillsRepository
import com.example.kdmeudinheiro.repository.UserRepository
import com.google.firebase.auth.FirebaseUser

class BillsViewModel : ViewModel() {
    val billRepo = BillsRepository()
    val userRepo = UserRepository()

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
        billRepo.getBills(idUser) { billList, e ->
            _billList.value = billList
            _error.value = e
        }
    }

    private val _addResponse = MutableLiveData<Boolean>()
    var addResponse: LiveData<Boolean> = _addResponse

    fun addBill(bill: BillsModel) {
        billRepo.addBills(bill) { resp ->
            _addResponse.value = resp
        }

    }
}
