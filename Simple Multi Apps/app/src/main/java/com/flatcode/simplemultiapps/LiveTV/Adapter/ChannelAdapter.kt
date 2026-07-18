package com.flatcode.simplemultiapps.livetv.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.flatcode.simplemultiapps.livetv.model.Channel
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.utils.CLASS
import com.flatcode.simplemultiapps.utils.VOID

class ChannelAdapter(private val type: String) :
    ListAdapter<Channel, ChannelAdapter.ViewHolder>(ChannelDiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return when (type) {
            "slider" -> R.layout.item_live_tv_slider
            "details" -> R.layout.item_live_tv
            else -> R.layout.item_live_tv_home
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val channel = getItem(position) ?: return

        holder.name.text = channel.name
        VOID.loadImage(holder.itemView.context, channel.thumbnail, holder.image)

        holder.itemView.setOnClickListener { view ->
            val intent = Intent(view.context, CLASS.LIVE_TV_DETAILS).apply {
                putExtra("channel", channel)
            }
            view.context.startActivity(intent)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.image)
        val name: TextView = itemView.findViewById(R.id.name)
    }

    class ChannelDiffCallback : DiffUtil.ItemCallback<Channel>() {
        override fun areItemsTheSame(oldItem: Channel, newItem: Channel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Channel, newItem: Channel): Boolean {
            return oldItem == newItem
        }
    }
}