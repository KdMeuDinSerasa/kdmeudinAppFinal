package com.example.kdmeudinheiro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.kdmeudinheiro.databinding.ActivityRegisterBinding
import com.example.kdmeudinheiro.model.UserModel
import com.example.kdmeudinheiro.repository.UserRepository

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val mUserRepository = UserRepository()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        loadComponents()
    }

    fun loadComponents() {
        binding.btnRegister.setOnClickListener {
            checkLogin()
        }
        binding.btnCancel.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }


    fun checkLogin(){
        if (binding.etEmail.text.toString().contains("@") && !binding.etEmail.text.toString()
                .isNullOrBlank() && !binding.etPassword.text.toString()
                .isNullOrBlank()) {
            if (!binding.etEmail.text.toString()
                    .isNullOrBlank() && !binding.etPassword.text.toString()
                    .isNullOrBlank()
            ) {
                val mUser =
                    UserModel(binding.etEmail.text.toString(), binding.etPassword.text.toString())
                mUserRepository.createUserWithEmailPassword(mUser) { user, error ->
                    if (user != null) {
                        startActivity(Intent(this, LoginActivity::class.java))
                        Toast.makeText(this, "Cadastrado com Sucesso", Toast.LENGTH_SHORT).show()
                    }
                    if (error != null) {
                        Toast.makeText(this, "Usuario Não Encontrado", Toast.LENGTH_SHORT).show()
                    }

                }

            } else Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
        }
        else Toast.makeText(this, "Email Invalido", Toast.LENGTH_SHORT).show()

    }


}