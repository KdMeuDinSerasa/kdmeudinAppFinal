package com.example.kdmeudinheiro.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kdmeudinheiro.model.BillsModel
import com.example.kdmeudinheiro.repository.BillsRepository

class BillsViewModel : ViewModel() {
    val billRepo = BillsRepository()

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

    fun addBill(bill: BillsModel){
        billRepo.addBills(bill){ resp ->
            _addResponse.value = resp
        }

    }
}
