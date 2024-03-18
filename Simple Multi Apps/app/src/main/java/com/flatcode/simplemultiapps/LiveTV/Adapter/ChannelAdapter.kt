package com.flatcode.simplemultiapps.LiveTV.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.flatcode.simplemultiapps.LiveTV.Model.Channel
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.Unit.CLASS
import com.flatcode.simplemultiapps.Unit.VOID

class ChannelAdapter(var channels: List<Channel>, var type: String) :
    RecyclerView.Adapter<ChannelAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View
        v = when (type) {
            "slider" -> {
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_live_tv_slider, parent, false)
            }

            "details" -> {
                LayoutInflater.from(parent.context).inflate(R.layout.item_live_tv, parent, false)
            }

            else -> {
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_live_tv_home, parent, false)
            }
        }
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = channels[position].name
        VOID.Glide(null, channels[position].thumbnail, holder.image)

        holder.itemView.setOnClickListener { v: View ->
            val i = Intent(v.context, CLASS.LIVE_TV_DETAILS)
            i.putExtra("channel", channels[position])
            v.context.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return channels.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView
        var name: TextView

        init {
            image = itemView.findViewById(R.id.image)
            name = itemView.findViewById(R.id.name)
        }
    }
}