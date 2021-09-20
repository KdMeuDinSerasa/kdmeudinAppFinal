package com.example.kdmeudinheiro.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kdmeudinheiro.R
import com.example.kdmeudinheiro.databinding.NewsModelBinding
import com.example.kdmeudinheiro.interfaces.ClickNews
import com.example.kdmeudinheiro.model.NewsLetter

class AdapterNews(val mClick: ClickNews) :
    ListAdapter<NewsLetter, ViewHolderNews>(DiffUtilsNews()) {
    private val mList = mutableListOf<NewsLetter>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderNews {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_model, parent, false)
        return ViewHolderNews(view)
    }

    override fun onBindViewHolder(holder: ViewHolderNews, position: Int) {
        val news = getItem(position)
        holder.bind(news)
        holder.itemView.setOnClickListener {
            mClick.newsClicked(news)
        }
    }

    fun update(newList: List<NewsLetter>) {
        mList.addAll(newList)
        submitList(mList.toMutableList())
    }
}

class DiffUtilsNews : DiffUtil.ItemCallback<NewsLetter>() {
    override fun areItemsTheSame(oldItem: NewsLetter, newItem: NewsLetter): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: NewsLetter, newItem: NewsLetter): Boolean {
        return oldItem == newItem
    }

}

class ViewHolderNews(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding = NewsModelBinding.bind(itemView)
    fun bind(mNewsLetter: NewsLetter) {
        binding.tvTitleNews.text = mNewsLetter.title
        Glide.with(binding.root).load(mNewsLetter.imageUrl).into(binding.ivImageNews)
        binding.tvSourceNews.text = mNewsLetter.source.siteName
        binding.tvDateNews.text = mNewsLetter.date.substring(0, 10).replace("-", "/")

    }
}
