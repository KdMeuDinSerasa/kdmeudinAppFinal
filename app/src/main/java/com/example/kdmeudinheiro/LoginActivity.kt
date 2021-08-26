package com.example.kdmeudinheiro

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.edit
import com.bumptech.glide.Glide
import com.example.kdmeudinheiro.databinding.ActivityLoginBinding
import com.example.kdmeudinheiro.enums.KeysShared
import com.example.kdmeudinheiro.model.UserModel
import com.example.kdmeudinheiro.repository.UserRepository

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val mUserRepository = UserRepository()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mSharedPreferences = getSharedPreferences(KeysShared.APP.key, Context.MODE_PRIVATE)


        if (mSharedPreferences.getBoolean(KeysShared.REMEMBERME.key, false))
            checkSession()

        loadComponents()
    }

    fun loadComponents() {

        binding.btnRegister.setOnClickListener {
            val initi = Intent(this, RegisterActivity::class.java)
            startActivity(initi)
        }
        binding.btnLogin.setOnClickListener {
            checkLogin()
        }


    }

    /**
     * Checa se a sessão esta ativa ou expirou somente se o checkbox remember-me tiver
     * sido marcado em uma sessão anterior
     */
    fun checkSession() {
        if (mUserRepository.getSession() != null) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    /**
     * Checa as credenciais do usuario, se preencheu todos os campos e se existe um usuario
     * cadastrado com as credenciais informadas
     */
    fun checkLogin() {
        if (!binding.etEmail.text.toString().isNullOrBlank() && !binding.etPassword.text.toString()
                .isNullOrBlank()
        ) {
            val mUser =
                UserModel(binding.etEmail.text.toString(), binding.etPassword.text.toString())
            mUserRepository.loginWithEmailPassword(mUser) { user, error ->
                if (user != null) {
                    if (binding.cbRememberMe.isChecked) {
                        val mSharedPreferences =
                            getSharedPreferences(KeysShared.APP.key, Context.MODE_PRIVATE)
                        mSharedPreferences.edit {
                            this.putBoolean(KeysShared.REMEMBERME.key, true)
                        }

                    }
                    startActivity(Intent(this, MainActivity::class.java))

                }
                if (error != null) {
                    Toast.makeText(this, "Usuario Não Encontrado", Toast.LENGTH_SHORT).show()
                }
            }

        } else Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()


    }
}
