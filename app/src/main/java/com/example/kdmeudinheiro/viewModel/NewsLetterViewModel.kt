package com.example.kdmeudinheiro.viewModel

import androidx.lifecycle.ViewModel
import com.example.kdmeudinheiro.repository.NewsLetterRepository
import javax.inject.Inject

class NewsLetterViewModel @Inject constructor(private val repository: NewsLetterRepository) : ViewModel() {

}