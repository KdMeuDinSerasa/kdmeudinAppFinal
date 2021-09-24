package com.example.kdmeudinheiro.viewModel

import android.content.Context
import android.net.Uri
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kdmeudinheiro.model.UserModel
import com.example.kdmeudinheiro.repository.BillsRepository
import com.example.kdmeudinheiro.repository.UserRepository
import com.example.kdmeudinheiro.services.NotificationHandler
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class UserPreferencesViewModel @Inject constructor(
    private val mUserRepository: UserRepository,
    private val mNotificationHandler: NotificationHandler,
    private val mBillsRepository: BillsRepository
) : ViewModel() {

    private var _mUserModel = MutableLiveData<UserModel>()
    val mUserModel: LiveData<UserModel> = _mUserModel

    private var _mFirebaseUser = MutableLiveData<FirebaseUser?>()
    val mFirebaseUser: LiveData<FirebaseUser?> = _mFirebaseUser

    private var _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private var _imgUser = MutableLiveData<Uri>()
    val imgUser: LiveData<Uri> = _imgUser


    fun uploadImgToFirebase(img: String, imgUri: Uri) {
        mUserRepository.uploadImgToFirebase(img, imgUri) { img, error ->
            if (img != null) _imgUser.value = img
            if (error != null) _error.value = error
        }
    }

    fun userLoged() {
        mUserRepository.getSession().apply {
            _mFirebaseUser.value = this
        }
    }

    fun editUser(mUserModel: UserModel) {
        viewModelScope.launch {
            mUserRepository.editUser(mUserModel)
        }

    }

    fun getUserById(id: String) {
        viewModelScope.launch {
            _mUserModel.value = mUserRepository.getUserById(id)
        }

    }

    fun sendNotifications(usedId: String?, acceptNotifications: Boolean, context: Context) {
        viewModelScope.launch {
            val calendar = Calendar.getInstance()
            var count = 0
            var toExpire = 0
            val notificationManager = NotificationManagerCompat.from(context)
            if (acceptNotifications) {
                if (usedId != null) {
                    mBillsRepository.getBills(usedId)?.forEach {
                        if (it.checkExpired()) count++
                        else if (it.checkToExpire()){
                            toExpire++
                        }
                    }
                    if (count > 0) mNotificationHandler.createNotification(
                        "Você possui contas vencidas",
                        "Total de contas vencidas: $count"
                    ).apply {
                        notificationManager.notify(1, this)

                    } else if (toExpire > 0) {
                        mNotificationHandler.createNotification(
                            "Você possui contas há vencer",
                            "Total de contas há vencer: $toExpire"
                        ).apply {
                            notificationManager.notify(1, this)
                        }
                    }
                }
            }
        }
    }
}