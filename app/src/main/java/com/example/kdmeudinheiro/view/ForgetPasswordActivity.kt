package com.example.kdmeudinheiro.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import com.example.kdmeudinheiro.R
import com.example.kdmeudinheiro.databinding.ActivityForgetPasswordBinding
import com.example.kdmeudinheiro.utils.feedback
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
        loadViewModels()
        loadComponents()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    fun loadViewModels(){
        viewModel.success.observe(this, {
            if (it) feedback(binding.root, R.string.redefinition_email_send, R.color.success)
        })
        viewModel.failure.observe(this, {
            feedback(binding.root, R.string.redefinition_email_not_found, R.color.failure)
        })
    }

    fun loadComponents(){
        binding.btnSendEmail.setOnClickListener {
            viewModel.sendRedefinitionEmail(binding.tvEmail.text.toString())
        }
    }
}