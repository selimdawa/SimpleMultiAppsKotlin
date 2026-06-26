package com.flatcode.simplemultiapps.NewsApp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.flatcode.simplemultiapps.NewsApp.Model.NewsHeadlines
import com.flatcode.simplemultiapps.NewsApp.SelectListener
import com.flatcode.simplemultiapps.Unit.DATA
import com.flatcode.simplemultiapps.Unit.VOID
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

            VOID.Glide(context, item.urlToImage, image)

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