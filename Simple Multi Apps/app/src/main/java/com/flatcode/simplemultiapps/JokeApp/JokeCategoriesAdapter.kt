package com.flatcode.simplemultiapps.JokeApp

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.Unit.DATA
import com.flatcode.simplemultiapps.databinding.ItemJokeCategoryBinding

class JokeCategoriesAdapter(private val context: Context, var categories: List<String>) :
    RecyclerView.Adapter<JokeCategoriesAdapter.ViewHolder>() {

    private var binding: ItemJokeCategoryBinding? = null
    var selected_position = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemJokeCategoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding!!.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.catName.text = categories[position]

        if (selected_position == position) {
            holder.card.setBackgroundResource(R.drawable.button_profile2)
            holder.catName.setTextColor(Color.WHITE)
        } else {
            holder.card.setBackgroundResource(R.drawable.button_profile)
            holder.catName.setTextColor(Color.BLACK)
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        var catName: TextView
        var card: CardView

        override fun onClick(v: View) {
            // Below line is just like a safety check, because sometimes holder could be null,
            // in that case, getAdapterPosition() will return RecyclerView.NO_POSITION
            if (adapterPosition == RecyclerView.NO_POSITION) return

            // Updating old as well as new positions
            notifyItemChanged(selected_position)
            selected_position = adapterPosition
            notifyItemChanged(selected_position)

            // Do your another stuff for your onClick
            if (categories[selected_position] === "Any")
                loadFragment(JokesFragment(DATA.JOKE_URL + "Any?amount=10"), v)
            if (categories[selected_position] === "Programming")
                loadFragment(JokesFragment(DATA.JOKE_URL + "Programming?amount=10"), v)
            if (categories[selected_position] === "Dark")
                loadFragment(JokesFragment(DATA.JOKE_URL + "Dark?amount=10"), v)
            if (categories[selected_position] === "Spooky")
                loadFragment(JokesFragment(DATA.JOKE_URL + "Spooky?amount=10"), v)
            if (categories[selected_position] === "Misc")
                loadFragment(JokesFragment(DATA.JOKE_URL + "Misc?amount=10"), v)
            if (categories[selected_position] === "Pun")
                loadFragment(JokesFragment(DATA.JOKE_URL + "Programming?amount=10"), v)
            if (categories[selected_position] === "Christmas")
                loadFragment(JokesFragment(DATA.JOKE_URL + "Christmas?amount=10"), v)
        }

        init {
            itemView.setOnClickListener(this)
            catName = binding!!.categoriesName
            card = binding!!.card
        }
    }

    fun loadFragment(fragment: Fragment?, v: View) {
        val activity = v.context as AppCompatActivity
        val manager = activity.supportFragmentManager
        val transaction = manager.beginTransaction().replace(R.id.fragment, fragment!!)
        transaction.commit()
    }
}