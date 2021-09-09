package com.example.kdmeudinheiro.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kdmeudinheiro.model.UserModel
import com.example.kdmeudinheiro.repository.UserRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class UserPreferencesViewModel : ViewModel() {

    private var _mUserModel = MutableLiveData<UserModel>()
    val mUserModel: LiveData<UserModel> = _mUserModel

    private var _mFirebaseUser = MutableLiveData<FirebaseUser?>()
    val mFirebaseUser: LiveData<FirebaseUser?> = _mFirebaseUser

    private val mUserRepository = UserRepository()


    fun userLoged(){
        mUserRepository.getSession().apply {
            _mFirebaseUser.value = this
        }
    }

    fun editUser(mUserModel: UserModel){
        viewModelScope.launch {
            mUserRepository.editUser(mUserModel)
        }

    }


    fun getUserById(id: String){
        viewModelScope.launch {
            _mUserModel.value = mUserRepository.getUserById(id)
        }

    }
}