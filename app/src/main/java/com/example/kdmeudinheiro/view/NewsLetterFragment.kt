package com.example.kdmeudinheiro.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kdmeudinheiro.R
import com.example.kdmeudinheiro.viewModel.NewsLetterViewModel

class NewsLetterFragment : Fragment() {

    companion object {
        fun newInstance() = NewsLetterFragment()
    }

    private lateinit var viewModel: NewsLetterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.news_letter_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NewsLetterViewModel::class.java)
        // TODO: Use the ViewModel
    }

}