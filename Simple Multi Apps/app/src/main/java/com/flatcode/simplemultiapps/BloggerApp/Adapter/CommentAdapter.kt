package com.flatcode.simplemultiapps.BloggerApp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.flatcode.simplemultiapps.BloggerApp.Model.Comment
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.Unit.DATA
import com.flatcode.simplemultiapps.Unit.VOID
import com.flatcode.simplemultiapps.databinding.ItemBloggerCommentBinding
import java.text.SimpleDateFormat

class CommentAdapter(private val context: Context, var comments: ArrayList<Comment>) :
    RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    private var binding: ItemBloggerCommentBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemBloggerCommentBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding!!.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = comments[position]
        val id = list.id
        val name = list.name
        val published = list.published
        val comment = list.comment
        val image = list.profileImage
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val dateFormat2 = SimpleDateFormat("dd/MM/yyyy K:mm a")
        var formattedDate = DATA.EMPTY

        try {
            val date = dateFormat.parse(published)
            formattedDate = dateFormat2.format(date)
        } catch (e: Exception) {
            formattedDate = published!!
            e.printStackTrace()
        }

        holder.name.text = name
        holder.date.text = formattedDate
        holder.comment.text = comment

        try {
            VOID.Glide(context, image, holder.image)
        } catch (e: Exception) {
            holder.image.setImageResource(R.drawable.ic_person)
        }
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var date: TextView
        var comment: TextView
        var image: ImageView

        init {
            name = binding!!.name
            date = binding!!.date
            comment = binding!!.comment
            image = binding!!.image
        }
    }
}