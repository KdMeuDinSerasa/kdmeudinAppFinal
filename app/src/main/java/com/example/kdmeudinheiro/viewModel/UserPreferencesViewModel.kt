package com.example.kdmeudinheiro.viewModel

import android.net.Uri
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
class UserPreferencesViewModel @Inject constructor(private val mUserRepository: UserRepository) : ViewModel() {

    private var _mUserModel = MutableLiveData<UserModel>()
    val mUserModel: LiveData<UserModel> = _mUserModel

    private var _mFirebaseUser = MutableLiveData<FirebaseUser?>()
    val mFirebaseUser: LiveData<FirebaseUser?> = _mFirebaseUser

    private var _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private var _imgUser = MutableLiveData<Uri>()
    val imgUser: LiveData<Uri> = _imgUser




    fun uploadImgToFirebase(img: String, imgUri: Uri){
        mUserRepository.uploadImgToFirebase(img, imgUri){ img, error ->
            if (img != null) _imgUser.value = img
            if (error != null) _error.value = error
        }
    }

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