package com.flatcode.simplemultiapps.JokeApp.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.flatcode.simplemultiapps.JokeApp.Fragment.JokesFragment
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.Unit.DATA
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
                categoriesName.setTextColor(Color.WHITE)
            } else {
                card.setBackgroundResource(R.drawable.button_profile)
                categoriesName.setTextColor(Color.BLACK)
            }

            root.setOnClickListener {
                val adapterPos = holder.bindingAdapterPosition
                if (adapterPos == RecyclerView.NO_POSITION) return@setOnClickListener

                notifyItemChanged(selectedPosition)
                selectedPosition = adapterPos
                notifyItemChanged(selectedPosition)

                val endpoint = if (currentCategory == "Pun") "Programming" else currentCategory
                val fragmentUrl = "${DATA.JOKE_URL}$endpoint?amount=10"

                loadFragment(JokesFragment(fragmentUrl), root)
            }
        }
    }

    override fun getItemCount(): Int = categories.size

    private fun loadFragment(fragment: Fragment, view: android.view.View) {
        val activity = view.context as? AppCompatActivity
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.fragment, fragment)
            ?.commit()
    }

    class ViewHolder(val binding: ItemJokeCategoryBinding) : RecyclerView.ViewHolder(binding.root)
}