package com.example.kdmeudinheiro.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kdmeudinheiro.model.NewsLetter
import com.example.kdmeudinheiro.model.ReciviedArticles
import com.example.kdmeudinheiro.repository.NewsLetterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsLetterViewModel @Inject constructor(private val repository: NewsLetterRepository) : ViewModel() {

    private val _mNewsList = MutableLiveData<ReciviedArticles>()
    val mNewsList: LiveData<ReciviedArticles> = _mNewsList

    fun getNews(page: Int){
        viewModelScope.launch {
            _mNewsList.value = repository.getNews(page)
        }

    }
}