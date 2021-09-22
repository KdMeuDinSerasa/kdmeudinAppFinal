package com.example.kdmeudinheiro.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import com.example.kdmeudinheiro.R
import com.example.kdmeudinheiro.databinding.ActivityLoginBinding
import com.example.kdmeudinheiro.enums.KeysShared
import com.example.kdmeudinheiro.model.UserModel
import com.example.kdmeudinheiro.viewModel.LoginViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var bottomSheetView: View
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        setContentView(binding.root)

        loadViewModels()

        loadComponents()
    }

    fun loadViewModels() {
        /**
         * Checa se a sessão esta ativa ou expirou somente se o checkbox remember-me tiver
         * sido marcado em uma sessão anterior
         */

        viewModel.mFirebaseUser.observe(this, {
            if (it != null) {
                startActivity(Intent(this, MainActivity::class.java))
            }
        })

        viewModel.mFirebaseUserLoged.observe(this, {
            if (it != null) {
                val mSharedPreferences =
                    getSharedPreferences(KeysShared.APP.key, Context.MODE_PRIVATE)
                if (binding.cbRememberMe.isChecked) {
                    mSharedPreferences.edit {
                        this.putBoolean(KeysShared.REMEMBERME.key, true)
                    }
                }
                mSharedPreferences.edit {
                    this.putString(KeysShared.USERID.key, it.uid)
                }
                startActivity(Intent(this, MainActivity::class.java))
            }
        })

        viewModel.error.observe(this, {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
        viewModel.result.observe(this, {
            if (it) Toast.makeText(this, "Usuario cadastrado com sucesso", Toast.LENGTH_SHORT)
                .show()
            else Toast.makeText(this, "Erro ao cadastrar", Toast.LENGTH_SHORT).show()
        })
    }

    fun loadComponents() {

        binding.createRegister.setOnClickListener {
            loadBottomSheet()
        }
        binding.btnLogin.setOnClickListener {
            val mUser = UserModel(
                id = "",
                email = binding.etEmail.editText?.text.toString(),
                password = binding.etPassword.editText?.text.toString(),
                name = "",
                null
            )
            if (mUser.checkLogin()) {
                viewModel.loginWithEmailEPassword(mUser)
            } else {
                Snackbar.make(binding.root, "Email ou senha invalidos", Snackbar.LENGTH_LONG).show()
            }
        }
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
        val mUser = UserModel(
            email = bottomSheetView.findViewById<EditText>(R.id.etEmailUserRegister).text.toString(),
            password = bottomSheetView.findViewById<EditText>(R.id.etPasswordUserRegister).text.toString(),
            name = bottomSheetView.findViewById<EditText>(R.id.etNameUserRegister).text.toString()
        )
        if (mUser.checkInsertData()) {
            viewModel.createUserWithEmailEPassword(mUser)
        } else {
            Snackbar.make(
                binding.root,
                "Favor inserir email ou senha validos",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }
}

