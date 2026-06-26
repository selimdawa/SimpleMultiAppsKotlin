package com.flatcode.simplemultiapps.BloggerApp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.flatcode.simplemultiapps.BloggerApp.Model.Label
import com.flatcode.simplemultiapps.Unit.DATA
import com.flatcode.simplemultiapps.databinding.ItemBloggerLabelBinding

class LabelAdapter(private val context: Context, var labels: ArrayList<Label>) :
    RecyclerView.Adapter<LabelAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBloggerLabelBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentLabel = labels[position]

        with(holder.binding) {
            label.text = currentLabel.label ?: DATA.EMPTY
        }
    }

    override fun getItemCount(): Int = labels.size

    class ViewHolder(val binding: ItemBloggerLabelBinding) : RecyclerView.ViewHolder(binding.root)
}