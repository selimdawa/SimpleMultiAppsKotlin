package com.flatcode.simplemultiapps.BloggerApp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.flatcode.simplemultiapps.BloggerApp.Model.Label
import com.flatcode.simplemultiapps.databinding.ItemBloggerLabelBinding

class LabelAdapter(private val context: Context, var labels: ArrayList<Label>) :
    RecyclerView.Adapter<LabelAdapter.ViewHolder>() {

    private var binding: ItemBloggerLabelBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemBloggerLabelBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding!!.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = labels[position]
        val label = list.label
        holder.label.text = label
    }

    override fun getItemCount(): Int {
        return labels.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var label: TextView

        init {
            label = binding!!.label
        }
    }
}