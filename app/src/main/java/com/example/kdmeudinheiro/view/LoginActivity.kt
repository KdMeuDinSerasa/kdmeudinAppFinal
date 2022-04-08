package com.example.kdmeudinheiro.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.KeyEvent
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
import com.example.kdmeudinheiro.utils.Constants
import com.example.kdmeudinheiro.utils.checkConnection
import com.example.kdmeudinheiro.utils.feedback
import com.example.kdmeudinheiro.utils.hideKeyboard
import com.example.kdmeudinheiro.viewModel.LoginViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var bottomSheetView: View
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var viewModel: LoginViewModel
    private lateinit var mSharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!checkConnection()) startActivity(Intent(this, NoConnectionActivity::class.java))
        binding = ActivityLoginBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        setContentView(binding.root)
        loadViewModelsObservers()
        loadComponents()
    }


    fun loadViewModelsObservers() {
        /**
         * checks if user session still active or not, then start the correct activity.
         */

        viewModel.mFirebaseUser.observe(this, {
            if (it != null) {
                startMainActivity()
            }
        })

        viewModel.mFirebaseUserLoged.observe(this, { saveLoginPreferences(it) })

        viewModel.error.observe(this, { showErrorOnToast(it) })

        viewModel.result.observe(this, { showFeedback(it) })
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun saveLoginPreferences(userFound: FirebaseUser?) {
        if (userFound != null) {
            mSharedPreferences = getSharedPreferences(KeysShared.APP.key, Context.MODE_PRIVATE)
            if (binding.cbRememberMe.isChecked) {
                mSharedPreferences.edit {
                    this.putBoolean(KeysShared.REMEMBERME.key, true)
                }
            }
            mSharedPreferences.edit {
                this.putString(KeysShared.USERID.key, userFound.uid)
            }
            startMainActivity()
        }
    }

    private fun showErrorOnToast(err: String) {
        Toast.makeText(this, err, Toast.LENGTH_SHORT).show()
    }

    private fun showFeedback(feedType: Boolean) {

        if (feedType) feedback(
            binding.root,
            R.string.registration_on_firebase_success,
            R.color.success
        )
        else feedback(binding.root, R.string.registration_on_firebase_failure, R.color.failure)
    }

    private fun loadComponents() {

        binding.etPassword.setOnKeyListener { view, keycode, keyEvent ->
            if (keycode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                hideKeyboard()
                return@setOnKeyListener true
            }

            return@setOnKeyListener false
        }

        binding.tvForgetPassword.setOnClickListener { startForgotPassword() }

        binding.createRegister.setOnClickListener { loadBottomSheet() }

        binding.btnLogin.setOnClickListener { attemptToLogin() }
    }


    private fun loadBottomSheet() {

        bottomSheetView = View.inflate(this, R.layout.activity_register, null)
        bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
        bottomSheetView

        loadBottomSheetComponents()
    }

    private fun loadBottomSheetComponents() {
        bottomSheetView.findViewById<Button>(R.id.btnRegister).setOnClickListener {
            checkLoginRegister()
        }
        bottomSheetView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            bottomSheetDialog.dismiss()
        }
    }

    private fun startForgotPassword() {
        startActivity(Intent(this, ForgetPasswordActivity::class.java))
    }

    private fun attemptToLogin() {
        val mUser = UserModel(
            id = Constants.EMPYT,
            email = binding.etEmail.editText?.text.toString(),
            password = binding.etPassword.editText?.text.toString(),
            name = Constants.EMPYT,
            null
        )
        if (mUser.checkLogin()) {
            viewModel.loginWithEmailEPassword(mUser)
        } else {
            feedback(binding.root, R.string.validation_login_failure, R.color.failure)
        }
    }

    private fun checkLoginRegister() {
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

