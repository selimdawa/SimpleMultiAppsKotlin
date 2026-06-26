package com.flatcode.simplemultiapps.JokeApp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.flatcode.simplemultiapps.JokeApp.Model.Joke
import com.flatcode.simplemultiapps.databinding.ItemJokeBinding

class JokeAdapter(
    var jokes: List<Joke>
) : RecyclerView.Adapter<JokeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemJokeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val jokeItem = jokes[position]

        with(holder.binding) {
            if (jokeItem.type == "single") {
                firstLine.text = jokeItem.joke
                secondLine.visibility = View.GONE
            } else {
                firstLine.text = jokeItem.setup
                secondLine.visibility = View.VISIBLE
                secondLine.text = jokeItem.delivery
            }
        }
    }

    override fun getItemCount(): Int = jokes.size

    class ViewHolder(val binding: ItemJokeBinding) : RecyclerView.ViewHolder(binding.root)
}