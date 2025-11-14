package com.flatcode.simplemultiapps.JokeApp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.flatcode.simplemultiapps.databinding.ItemJokeBinding

class JokeAdapter(
    private val context: Context?,
    jokes: List<Joke>,
) : RecyclerView.Adapter<JokeAdapter.ViewHolder>() {
    private var binding: ItemJokeBinding? = null
    var jokes: List<Joke>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemJokeBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding!!.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (jokes[position].type == "single") {
            holder.firstLine.text = jokes[position].joke
            holder.secondLine.visibility = View.GONE
        } else {
            holder.firstLine.text = jokes[position].setup
            holder.secondLine.visibility = View.VISIBLE
            holder.secondLine.text = jokes[position].delivery
        }
    }

    override fun getItemCount(): Int {
        return jokes.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var firstLine: TextView
        var secondLine: TextView

        init {
            firstLine = binding!!.firstLine
            secondLine = binding!!.secondLine
        }
    }

    init {
        this.jokes = jokes
    }
}