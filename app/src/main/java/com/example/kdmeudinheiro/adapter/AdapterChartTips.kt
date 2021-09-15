package com.example.kdmeudinheiro.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kdmeudinheiro.R
import com.example.kdmeudinheiro.databinding.ItemListChartBinding
import com.example.kdmeudinheiro.model.Articles

class AdapterChartTips( val onClickItem: (Articles) -> Unit) :
    RecyclerView.Adapter<ChartTipsViewHolder>() {

    private val listOfArticles = mutableListOf<Articles>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartTipsViewHolder {
        LayoutInflater.from(parent.context).inflate(R.layout.item_list_chart, parent, false).apply {
            return ChartTipsViewHolder(this)
        }
    }

    override fun onBindViewHolder(holder: ChartTipsViewHolder, position: Int) {
        listOfArticles[position].apply {
            holder.bind(this)
            holder.itemView.setOnClickListener { onClickItem(this) }
        }

    }

    override fun getItemCount(): Int = listOfArticles.size

    fun update(newList: List<Articles>) {
        listOfArticles.clear()
        listOfArticles.addAll(newList)
        notifyDataSetChanged()
    }
}

class ChartTipsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var binding = ItemListChartBinding.bind(itemView)

    fun bind(article: Articles) {
        binding.textViewNameArticle.text = article.name
        binding.imageViewArticle.apply {
            Glide.with(this)
                .load(article.image)
                .placeholder(R.drawable.ic_baseline_help_24)
                .into(this)
        }
    }
}