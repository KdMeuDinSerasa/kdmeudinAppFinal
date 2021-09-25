package com.example.kdmeudinheiro.view

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
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
import com.example.kdmeudinheiro.databinding.OfflineLayoutBinding
import com.example.kdmeudinheiro.enums.KeysShared
import com.example.kdmeudinheiro.model.UserModel
import com.example.kdmeudinheiro.utils.checkConnection
import com.example.kdmeudinheiro.utils.feedback
import com.example.kdmeudinheiro.viewModel.LoginViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var bottomSheetView: View
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!checkConnection()) startActivity(Intent(this, NoConnectionActivity::class.java))
        binding = ActivityLoginBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        setContentView(binding.root)
        loadViewModels()
        loadComponents()
    }



    fun loadViewModels() {
        /**
         * checks if user session still active or not, then start the correct activity.
         */

        viewModel.mFirebaseUser.observe(this, {
            if (it != null) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
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
                finish()
            }
        })

        viewModel.error.observe(this, {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
        viewModel.result.observe(this, {
            if (it) feedback(
                binding.root,
                R.string.registration_on_firebase_success,
                R.color.success
            )
            else feedback(binding.root, R.string.registration_on_firebase_failure, R.color.failure)
        })
    }

    fun loadComponents() {

        binding.tvForgetPassword.setOnClickListener {
            startActivity(Intent(this, ForgetPasswordActivity::class.java))
        }
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
                feedback(binding.root, R.string.validation_login_failure, R.color.failure)
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
            bottomSheetDialog.dismiss()
            binding.etUserEmail.setText(mUser.email)
        } else {
            bottomSheetDialog.dismiss()
            feedback(binding.root, R.string.validation_registration_failure, R.color.failure)
        }
    }
}

