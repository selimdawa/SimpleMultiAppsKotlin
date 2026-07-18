package com.flatcode.simplemultiapps.newsapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.flatcode.simplemultiapps.newsapp.model.NewsHeadlines
import com.flatcode.simplemultiapps.newsapp.SelectListener
import com.flatcode.simplemultiapps.utils.DATA
import com.flatcode.simplemultiapps.utils.loadImage
import com.flatcode.simplemultiapps.databinding.ItemNewsBinding

class NewsAppAdapter(
    private val context: Context,
    private val headlines: List<NewsHeadlines?>?,
    private val listener: SelectListener
) : RecyclerView.Adapter<NewsAppAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = headlines?.get(position) ?: return

        with(holder.binding) {
            content.text = item.title
            source.text = item.source?.name ?: DATA.EMPTY

            image.loadImage(item.urlToImage)

            card.setOnClickListener {
                val currentPos = holder.bindingAdapterPosition
                if (currentPos != RecyclerView.NO_POSITION) {
                    listener.onNewsClicked(item)
                }
            }
        }
    }

    override fun getItemCount(): Int = headlines?.size ?: 0

    class ViewHolder(val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root)
}