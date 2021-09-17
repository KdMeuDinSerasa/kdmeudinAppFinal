package com.example.kdmeudinheiro.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kdmeudinheiro.R
import com.example.kdmeudinheiro.databinding.NewsLetterFragmentBinding
import com.example.kdmeudinheiro.viewModel.NewsLetterViewModel

class NewsLetterFragment : Fragment(R.layout.news_letter_fragment) {

    companion object {
        fun newInstance() = NewsLetterFragment()
    }

    private lateinit var viewModel: NewsLetterViewModel
    private lateinit var binding: NewsLetterFragmentBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = NewsLetterFragmentBinding.bind(view)
        viewModel = ViewModelProvider(this).get(NewsLetterViewModel::class.java)

    }



}