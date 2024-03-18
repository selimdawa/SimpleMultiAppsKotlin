package com.flatcode.simplemultiapps.MainApp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.Unit.DATA
import com.flatcode.simplemultiapps.databinding.ItemMainBinding
import java.text.MessageFormat

class MainAdapter(private val context: Context, list: List<Main>) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private var binding: ItemMainBinding? = null
    var list: List<Main>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemMainBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding!!.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: Main = list[position]
        val image: Int = model.image
        val number: Int = model.number
        val name: String = model.title!!
        //String id = list.getId();
        val c: Class<*> = model.c!!

        if (image != 0) {
            holder.image.setImageResource(image)
        } else {
            holder.image.setImageResource(R.drawable.ic_load)
        }
        if (number != 0) {
            holder.number.visibility = View.VISIBLE
            holder.number.text = MessageFormat.format("{0}{1}", DATA.EMPTY, number)
        } else {
            holder.number.visibility = View.GONE
        }
        holder.name.text = name

        holder.itemView.setOnClickListener {
            val intent = Intent(context, c)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var number: TextView
        var image: ImageView
        var item: LinearLayout

        init {
            image = binding!!.image
            name = binding!!.name
            number = binding!!.number
            item = binding!!.item
        }
    }

    init {
        this.list = list
    }
}
