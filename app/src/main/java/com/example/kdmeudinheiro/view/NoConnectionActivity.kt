package com.example.kdmeudinheiro.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kdmeudinheiro.databinding.OfflineLayoutBinding

class NoConnectionActivity : AppCompatActivity() {
    private lateinit var binding: OfflineLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OfflineLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.tvTryAgain.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }
}