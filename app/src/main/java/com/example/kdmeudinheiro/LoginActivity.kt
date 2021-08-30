package com.example.kdmeudinheiro

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.edit
import com.bumptech.glide.Glide
import com.example.kdmeudinheiro.databinding.ActivityLoginBinding
import com.example.kdmeudinheiro.databinding.ActivityRegisterBinding
import com.example.kdmeudinheiro.enums.KeysShared
import com.example.kdmeudinheiro.model.UserModel
import com.example.kdmeudinheiro.repository.UserRepository
import com.google.android.material.bottomsheet.BottomSheetDialog

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val mUserRepository = UserRepository()
    private lateinit var bottomSheetView: View
    private lateinit var bottomSheetDialog: BottomSheetDialog
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

        binding.btnCreateRegister.setOnClickListener {
            loadBottomSheet()
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

    fun loadBottomSheet() {

        bottomSheetView = View.inflate(this, R.layout.activity_register, null)
        bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
        bottomSheetView

        loadBottomSheetComponents()
    }

    fun loadBottomSheetComponents() {
        bottomSheetView.findViewById<Button>(R.id.btnRegister).setOnClickListener {
            checkLoginRegister()
        }
        bottomSheetView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            bottomSheetDialog.dismiss()
        }
    }

    fun checkLoginRegister() {
        val emailAux = bottomSheetView.findViewById<EditText>(R.id.etEmailRegister)
        val passwordAux = bottomSheetView.findViewById<EditText>(R.id.etPasswordRegister)

        if (emailAux.text.toString().isNullOrBlank() && passwordAux.text.toString().isNullOrBlank())
            Toast.makeText(this, "Preencha Todos os campos", Toast.LENGTH_SHORT).show()
        else {

            if (emailAux.text.toString()
                    .contains("@")

            ) {
                val mUser =
                    UserModel(binding.etEmail.text.toString(), binding.etPassword.text.toString())
                mUserRepository.createUserWithEmailPassword(mUser) { user, error ->
                    if (user != null) {
                        Toast.makeText(this, "Cadastrado com Sucesso", Toast.LENGTH_SHORT).show()
                        bottomSheetDialog.dismiss()
                    }
                    if (error != null) {
                        Toast.makeText(this, "Usuario Não Encontrado", Toast.LENGTH_SHORT).show()
                    }
                }

            } else Toast.makeText(this, "Email Invalido", Toast.LENGTH_SHORT).show()
        }
    }
}
