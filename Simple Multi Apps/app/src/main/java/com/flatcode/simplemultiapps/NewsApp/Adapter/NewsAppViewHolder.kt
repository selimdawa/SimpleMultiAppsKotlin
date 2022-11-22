package com.flatcode.simplemultiapps.NewsApp.Adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.flatcode.simplemultiapps.R

class NewsAppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var content: TextView
    var source: TextView
    var image: ImageView
    var card: CardView

    init {
        content = itemView.findViewById(R.id.content)
        source = itemView.findViewById(R.id.source)
        image = itemView.findViewById(R.id.image)
        card = itemView.findViewById(R.id.card)
    }
}