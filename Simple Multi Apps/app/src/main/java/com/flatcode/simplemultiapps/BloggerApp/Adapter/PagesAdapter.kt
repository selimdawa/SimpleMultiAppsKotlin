package com.flatcode.simplemultiapps.BloggerApp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.flatcode.simplemultiapps.BloggerApp.Model.Page
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.Unit.CLASS
import com.flatcode.simplemultiapps.Unit.DATA
import com.flatcode.simplemultiapps.Unit.VOID
import com.flatcode.simplemultiapps.databinding.ItemBloggerBinding
import org.jsoup.Jsoup
import java.text.MessageFormat
import java.text.SimpleDateFormat

class PagesAdapter(private val context: Context, var pages: ArrayList<Page>) :
    RecyclerView.Adapter<PagesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBloggerBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = pages[position]
        val authorName = list.authorName
        val content = list.content
        val id = list.id
        val published = list.published
        val selfLink = list.selfLink
        val title = list.title
        val updated = list.updated
        val url = list.url
        val document = Jsoup.parse(content ?: DATA.EMPTY)

        try {
            val elements = document.select("img")
            val image = elements.attr("src")
            VOID.Glide(context, image, holder.binding.image)
        } catch (e: Exception) {
            holder.binding.image.setImageResource(R.color.image_profile)
        }
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val dateFormat2 = SimpleDateFormat("dd/MM/yyyy K:mm a")
        var formattedDate = DATA.EMPTY

        try {
            val date = dateFormat.parse(published)
            formattedDate = dateFormat2.format(date)
        } catch (e: Exception) {
            formattedDate = published ?: DATA.EMPTY
            e.printStackTrace()
        }

        holder.binding.title.text = title
        holder.binding.description.text = document.text()
        holder.binding.publishInfo.text =
            MessageFormat.format("By {0}{1}{2}", authorName, DATA.SPACE, formattedDate)
        holder.itemView.setOnClickListener {
            VOID.IntentExtra(context, CLASS.BLOGGER_PAGES_DETAILS, "pageId", id)
        }
    }

    override fun getItemCount(): Int {
        return pages.size
    }

    inner class ViewHolder(val binding: ItemBloggerBinding) : RecyclerView.ViewHolder(binding.root)
}