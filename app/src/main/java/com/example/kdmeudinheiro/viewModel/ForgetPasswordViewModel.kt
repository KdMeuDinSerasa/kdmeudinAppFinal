package com.example.kdmeudinheiro.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kdmeudinheiro.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ForgetPasswordViewModel @Inject constructor(
    private var mUserRepository: UserRepository
) : ViewModel() {

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> = _success

    private val _failure = MutableLiveData<String>()
    val failure: LiveData<String> = _failure


    fun sendRedefinitionEmail(email: String) {
        viewModelScope.launch {
            try {
                mUserRepository.ResetUserPassword(email)
                _success.value = true
            } catch (e: Exception){
                _failure.value = "error"
            }


        }
    }

}