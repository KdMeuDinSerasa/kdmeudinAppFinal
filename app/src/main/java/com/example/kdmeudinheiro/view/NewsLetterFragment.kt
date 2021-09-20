package com.example.kdmeudinheiro.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kdmeudinheiro.R
import com.example.kdmeudinheiro.adapter.AdapterNews
import com.example.kdmeudinheiro.databinding.NewsLetterFragmentBinding
import com.example.kdmeudinheiro.interfaces.ClickNews
import com.example.kdmeudinheiro.model.NewsLetter
import com.example.kdmeudinheiro.viewModel.NewsLetterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsLetterFragment : Fragment(R.layout.news_letter_fragment), ClickNews {

    private lateinit var viewModel: NewsLetterViewModel
    private lateinit var binding: NewsLetterFragmentBinding
    private val adapter = AdapterNews(this)
    private var page: Int = 1
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = NewsLetterFragmentBinding.bind(view)
        viewModel = ViewModelProvider(this).get(NewsLetterViewModel::class.java)
        loadViewModels()
        loadComponents()
        viewModel.getNews(page)

    }

    fun loadViewModels() {
        viewModel.mNewsList.observe(viewLifecycleOwner, {
            adapter.update(it.news)
            binding.progressAnimation.visibility = View.GONE
        })
    }

    fun loadComponents() {
        binding.recyclerNews.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerNews.adapter = adapter

        binding.recyclerNews.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {

                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE && !recyclerView.canScrollVertically(
                        1
                    )
                ) {
                    page++
                    viewModel.getNews(page)
                    binding.progressAnimation.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun newsClicked(mNewsLetter: NewsLetter) {
        val browser = Intent(Intent.ACTION_VIEW, Uri.parse(mNewsLetter.url))
        startActivity(browser)
    }


}