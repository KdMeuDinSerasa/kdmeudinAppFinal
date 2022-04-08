package com.example.kdmeudinheiro.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import com.example.kdmeudinheiro.databinding.ActivitySplashBinding
import com.example.kdmeudinheiro.enums.KeysShared
import com.example.kdmeudinheiro.viewModel.LoginViewModel
import com.example.kdmeudinheiro.viewModel.MainViewModel
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var viewModelLogin: LoginViewModel
    private lateinit var viewModelMain: MainViewModel
    private lateinit var mSharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        mSharedPreferences = getSharedPreferences(KeysShared.APP.key, Context.MODE_PRIVATE)
        viewModelLogin = ViewModelProvider(this)[LoginViewModel::class.java]
        viewModelMain = ViewModelProvider(this)[MainViewModel::class.java]
        setContentView(binding.root)

        loadViewModelsObservers()

        if (mSharedPreferences.getBoolean(KeysShared.REMEMBERME.key, false)) {
            viewModelLogin.checkSession()
        } else
            Handler(Looper.getMainLooper()).postDelayed({
                startLoginActivity()
            }, 3000)
    }

   private fun loadViewModelsObservers() {
        viewModelLogin.mFirebaseUser.observe(this, { handlerUserInstance(it) })
        viewModelMain.mIncomeModel.observe(this, {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        })
    }

    private fun handlerUserInstance(userReceived: FirebaseUser?){
        if (userReceived != null) {
            mSharedPreferences.edit {
                this.putString(KeysShared.USERID.key, userReceived.uid)
            }
            viewModelMain.getUserById(userReceived.uid)
            Handler(Looper.getMainLooper()).postDelayed({
                viewModelMain.getIncome(userReceived.uid)
            }, 3000)
        } else {
            startLoginActivity()
        }
    }

    private fun startLoginActivity(){
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}