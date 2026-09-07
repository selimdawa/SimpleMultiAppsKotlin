package com.flatcode.simplemultiapps.jokeapp.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.flatcode.simplemultiapps.jokeapp.fragment.JokesFragment
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.utils.DATA
import com.flatcode.simplemultiapps.databinding.ItemJokeCategoryBinding

class JokeCategoriesAdapter(private val context: Context, var categories: List<String>) :
    RecyclerView.Adapter<JokeCategoriesAdapter.ViewHolder>() {

    var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemJokeCategoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentCategory = categories[position]

        with(holder.binding) {
            categoriesName.text = currentCategory

            if (selectedPosition == position) {
                card.setBackgroundResource(R.drawable.button_profile2)
                categoriesName.setTextColor(ContextCompat.getColor(context, R.color.white))
            } else {
                card.setBackgroundResource(R.drawable.button_profile)
                categoriesName.setTextColor(ContextCompat.getColor(context, R.color.black))
            }

            root.setOnClickListener {
                val adapterPos = holder.bindingAdapterPosition
                if (adapterPos == RecyclerView.NO_POSITION) return@setOnClickListener

                notifyItemChanged(selectedPosition)
                selectedPosition = adapterPos
                notifyItemChanged(selectedPosition)

                val endpoint = if (currentCategory == "Pun") "Programming" else currentCategory

                val fragment = JokesFragment().apply {
                    arguments = Bundle().apply {
                        putString(JokesFragment.KEY_JOKES_URL, "${DATA.JOKE_BASE_URL}$endpoint?amount=10")
                    }
                }

                loadFragment(fragment, root)
            }
        }
    }

    override fun getItemCount(): Int = categories.size

    private fun loadFragment(fragment: Fragment, view: View) {
        val activity = view.context as? AppCompatActivity
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.fragment, fragment)
            ?.commit()
    }

    class ViewHolder(val binding: ItemJokeCategoryBinding) : RecyclerView.ViewHolder(binding.root)
}