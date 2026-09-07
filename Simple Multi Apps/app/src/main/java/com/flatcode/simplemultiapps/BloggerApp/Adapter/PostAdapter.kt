package com.flatcode.simplemultiapps.bloggerapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.flatcode.simplemultiapps.bloggerapp.activity.PostDetailsActivity
import com.flatcode.simplemultiapps.bloggerapp.model.Post
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.utils.DATA
import com.flatcode.simplemultiapps.utils.intent1
import com.flatcode.simplemultiapps.utils.loadImage
import com.flatcode.simplemultiapps.databinding.ItemBloggerBinding
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.Locale

class PostAdapter(private val context: Context, var posts: ArrayList<Post>) :
    RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    private val inputDateFormat = SimpleDateFormat(DATA.INPUT_DATE_FORMAT, Locale.ENGLISH)
    private val outputDateFormat = SimpleDateFormat(DATA.OUTPUT_DATE_FORMAT, Locale.ENGLISH)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBloggerBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        val document = Jsoup.parse(post.content ?: DATA.EMPTY)

        try {
            val image = document.select(DATA.IMG).attr(DATA.SRC)
            holder.binding.image.loadImage(image)
        } catch (_: Exception) {
            holder.binding.image.setImageResource(R.color.image_profile)
        }

        val formattedDate = try {
            val date = inputDateFormat.parse(post.published ?: DATA.EMPTY)
            if (date != null) outputDateFormat.format(date) else post.published ?: DATA.EMPTY
        } catch (_: Exception) {
            post.published ?: DATA.EMPTY
        }

        with(holder.binding) {
            title.text = post.title ?: DATA.EMPTY
            description.text = document.text()

            val author = post.authorName ?: DATA.EMPTY
            publishInfo.text = context.getString(R.string.publish_info, author, formattedDate)
        }

        holder.itemView.setOnClickListener {
            context.intent1(PostDetailsActivity::class.java) {
                putExtra(DATA.POST_ID, post.id)
            }
        }
    }

    override fun getItemCount(): Int = posts.size

    class ViewHolder(val binding: ItemBloggerBinding) : RecyclerView.ViewHolder(binding.root)
}