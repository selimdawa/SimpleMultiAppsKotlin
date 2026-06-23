package com.flatcode.simplemultiapps.NewsApp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.flatcode.simplemultiapps.NewsApp.Model.NewsHeadlines
import com.flatcode.simplemultiapps.NewsApp.SelectListener
import com.flatcode.simplemultiapps.Unit.VOID
import com.flatcode.simplemultiapps.databinding.ItemNewsBinding

class NewsAppAdapter(
    private val context: Context,
    private val headlines: List<NewsHeadlines?>?,
    private val listener: SelectListener
) : RecyclerView.Adapter<NewsAppAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        headlines?.get(position)?.let { item ->
            holder.bind(item)
        }
    }

    override fun getItemCount(): Int = headlines?.size ?: 0

    inner class ViewHolder(private val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NewsHeadlines) {
            binding.content.text = item.title
            binding.source.text = item.source?.name ?: ""

            item.urlToImage.let { url ->
                VOID.Glide(context, url, binding.image)
            }

            binding.card.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onNewsClicked(item)
                }
            }
        }
    }
}