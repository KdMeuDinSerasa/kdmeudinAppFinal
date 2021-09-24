package com.example.kdmeudinheiro.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import com.example.kdmeudinheiro.databinding.ActivitySplashBinding
import com.example.kdmeudinheiro.enums.KeysShared
import com.example.kdmeudinheiro.services.NotificationHandler
import com.example.kdmeudinheiro.viewModel.LoginViewModel
import com.example.kdmeudinheiro.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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
        viewModelLogin = ViewModelProvider(this).get(LoginViewModel::class.java)
        viewModelMain = ViewModelProvider(this).get(MainViewModel::class.java)
        setContentView(binding.root)

        loadViewModels()

        if (mSharedPreferences.getBoolean(KeysShared.REMEMBERME.key, false)) {
            viewModelLogin.checkSession()
        } else
            Handler().postDelayed({
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }, 3000)


    }

    fun loadViewModels() {
        viewModelLogin.mFirebaseUser.observe(this, {
            if (it != null) {
                mSharedPreferences.edit {
                    this.putString(KeysShared.USERID.key, it.uid)
                }
                viewModelMain.getUserById(it.uid)
                Handler().postDelayed({
                    viewModelMain.getIncome(it.uid)
                }, 3000)
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }

        })
        viewModelMain.mIncomeModel.observe(this, {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        })
    }
}