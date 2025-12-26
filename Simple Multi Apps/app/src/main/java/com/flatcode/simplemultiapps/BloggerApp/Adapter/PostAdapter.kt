package com.flatcode.simplemultiapps.BloggerApp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.flatcode.simplemultiapps.BloggerApp.Model.Post
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.Unit.CLASS
import com.flatcode.simplemultiapps.Unit.DATA
import com.flatcode.simplemultiapps.Unit.VOID
import com.flatcode.simplemultiapps.databinding.ItemBloggerBinding
import org.jsoup.Jsoup
import java.text.MessageFormat
import java.text.SimpleDateFormat

class PostAdapter(private val context: Context, var posts: ArrayList<Post>) :
    RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    private var binding: ItemBloggerBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemBloggerBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding!!.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = posts[position]
        val authorName = list.authorName
        val content = list.content
        val id = list.id
        val published = list.published
        val selfLink = list.selfLink
        val title = list.title
        val updated = list.updated
        val url = list.url
        val document = Jsoup.parse(content!!)

        try {
            val elements = document.select("img")
            val image = elements[0].attr("src")
            VOID.Glide(context, image, holder.image)
        } catch (e: Exception) {
            holder.image.setImageResource(R.color.image_profile)
        }

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

        holder.title.text = title
        holder.description.text = document.text()
        holder.publishInfo.text =
            MessageFormat.format("By {0}{1}{2}", authorName, DATA.SPACE, formattedDate)
        holder.itemView.setOnClickListener {
            VOID.IntentExtra(context, CLASS.BLOGGER_POST_DETAILS, "postId", id)
        }
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView
        var publishInfo: TextView
        var description: TextView
        var image: ImageView

        init {
            title = binding!!.title
            publishInfo = binding!!.publishInfo
            description = binding!!.description
            image = binding!!.image
        }
    }
}