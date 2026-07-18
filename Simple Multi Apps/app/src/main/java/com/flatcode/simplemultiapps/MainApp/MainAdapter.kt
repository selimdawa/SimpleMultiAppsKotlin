package com.flatcode.simplemultiapps.mainapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.databinding.ItemMainBinding

class MainAdapter(private val context: Context, private val list: List<Main>) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]

        with(holder.binding) {
            if (model.image != 0) {
                image.setImageResource(model.image)
            } else {
                image.setImageResource(R.drawable.ic_load)
            }

            if (model.number != 0) {
                number.visibility = View.VISIBLE
                number.text = model.number.toString()
            } else {
                number.visibility = View.GONE
            }

            name.text = model.title.orEmpty()

            root.setOnClickListener {
                model.c?.let { targetClass ->
                    context.startActivity(Intent(context, targetClass))
                }
            }
        }
    }

    override fun getItemCount(): Int = list.size

    class ViewHolder(val binding: ItemMainBinding) : RecyclerView.ViewHolder(binding.root)
}