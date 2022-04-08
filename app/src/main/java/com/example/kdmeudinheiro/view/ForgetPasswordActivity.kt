package com.example.kdmeudinheiro.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import androidx.lifecycle.ViewModelProvider
import com.example.kdmeudinheiro.R
import com.example.kdmeudinheiro.databinding.ActivityForgetPasswordBinding
import com.example.kdmeudinheiro.utils.feedback
import com.example.kdmeudinheiro.utils.hideKeyboard
import com.example.kdmeudinheiro.viewModel.ForgetPasswordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgetPasswordBinding
    private lateinit var viewModel: ForgetPasswordViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(ForgetPasswordViewModel::class.java)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        loadViewModelsObservers()
        loadComponents()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }


    private fun loadViewModelsObservers(){
        viewModel.success.observe(this, { showSuccess(it) })

        viewModel.failure.observe(this, { showFailure() })
    }

    private fun showSuccess(show: Boolean){
        if (show) feedback(binding.root, R.string.redefinition_email_send, R.color.success)
    }

    private fun showFailure(){
        feedback(binding.root, R.string.redefinition_email_not_found, R.color.failure)
    }

    private fun loadComponents(){
        binding.btnSendEmail.setOnClickListener { sendRedefinitionEmail() }

        binding.tvEmail.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP){
                hideKeyboard()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }

    private fun sendRedefinitionEmail(){
        viewModel.sendRedefinitionEmail(binding.tvEmail.text.toString())
    }
}