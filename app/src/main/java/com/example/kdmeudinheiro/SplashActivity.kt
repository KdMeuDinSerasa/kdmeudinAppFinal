package com.example.kdmeudinheiro

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import com.example.kdmeudinheiro.databinding.ActivityLoginBinding
import com.example.kdmeudinheiro.databinding.ActivitySplashBinding
import com.example.kdmeudinheiro.enums.KeysShared
import com.example.kdmeudinheiro.view.MainActivity
import com.example.kdmeudinheiro.viewModel.LoginViewModel
import com.example.kdmeudinheiro.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var viewModelLogin: LoginViewModel
    private lateinit var viewModelMain: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val mSharedPreferences = getSharedPreferences(KeysShared.APP.key, Context.MODE_PRIVATE)
        viewModelLogin = ViewModelProvider(this).get(LoginViewModel::class.java)
        viewModelMain = ViewModelProvider(this).get(MainViewModel::class.java)
        setContentView(binding.root)

        loadViewModels()
        if (mSharedPreferences.getBoolean(KeysShared.REMEMBERME.key, false) == true){
            viewModelLogin.checkSession()
        } else
            startActivity(Intent(this, LoginActivity::class.java))

    }
    fun loadViewModels(){
        viewModelLogin.mFirebaseUser.observe(this, {
            val mSharedPreferences = getSharedPreferences(KeysShared.APP.key, Context.MODE_PRIVATE)
            if (it != null) {
                mSharedPreferences.edit {
                    this.putString(KeysShared.USERID.key, it.uid)
                }
                viewModelMain.getUserById(it.uid)
                viewModelMain.getIncome(it.uid)
            } else startActivity(Intent(this, LoginActivity::class.java))

        })
        viewModelMain.mIncomeModel.observe(this,{
            startActivity(Intent(this, MainActivity::class.java))
        })
    }
}