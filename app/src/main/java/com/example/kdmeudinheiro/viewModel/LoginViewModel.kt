package com.example.kdmeudinheiro.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kdmeudinheiro.model.UserModel
import com.example.kdmeudinheiro.repository.UserRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val mUserRepository: UserRepository
) : ViewModel() {

    private val _mFirebaseUserLoged = MutableLiveData<FirebaseUser?>()
    val mFirebaseUserLoged: LiveData<FirebaseUser?> = _mFirebaseUserLoged

    private val _mFirebaseUser = MutableLiveData<FirebaseUser?>()
    val mFirebaseUser: LiveData<FirebaseUser?> = _mFirebaseUser

    private val _result = MutableLiveData<Boolean>()
    val result: LiveData<Boolean> = _result

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun checkSession() {
        _mFirebaseUser.value = mUserRepository.getSession()
    }

    fun createUserWithEmailEPassword(userModel: UserModel) {
        mUserRepository.createUserWithEmailPassword(
            userModel.email,
            userModel.password
        ) { firebaseUser, erro ->
            if (firebaseUser != null) {
                val mUser = UserModel(
                    firebaseUser.uid,
                    userModel.email,
                    userModel.password,
                    userModel.name,
                    null
                )
                viewModelScope.launch {
                    var result = mUserRepository.addUser(mUser)
                    _result.value = result

                }
            }
        }
    }

    fun loginWithEmailEPassword(userModel: UserModel) {
        mUserRepository.loginWithEmailPassword(userModel.email, userModel.password) { user, erro ->
            if (user != null) _mFirebaseUserLoged.value = user
            if (erro != null) _error.value = erro
        }
    }
}