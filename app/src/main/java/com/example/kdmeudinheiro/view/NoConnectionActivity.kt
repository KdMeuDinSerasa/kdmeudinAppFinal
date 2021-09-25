package com.example.kdmeudinheiro.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kdmeudinheiro.databinding.OfflineLayoutBinding

class NoConnectionActivity : AppCompatActivity() {
    private lateinit var noConnection: OfflineLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noConnection = OfflineLayoutBinding.inflate(layoutInflater)
        setContentView(noConnection.root)
    }
}