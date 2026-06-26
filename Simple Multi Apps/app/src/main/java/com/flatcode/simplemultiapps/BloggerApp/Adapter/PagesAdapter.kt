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
import java.text.SimpleDateFormat
import java.util.Locale

class PagesAdapter(private val context: Context, var pages: ArrayList<Page>) :
    RecyclerView.Adapter<PagesAdapter.ViewHolder>() {

    private val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
    private val outputDateFormat = SimpleDateFormat("dd/MM/yyyy K:mm a", Locale.ENGLISH)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBloggerBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val page = pages[position]
        val document = Jsoup.parse(page.content ?: DATA.EMPTY)

        try {
            val image = document.select("img").attr("src")
            VOID.Glide(context, image, holder.binding.image)
        } catch (_: Exception) {
            holder.binding.image.setImageResource(R.color.image_profile)
        }

        val formattedDate = try {
            val date = inputDateFormat.parse(page.published ?: DATA.EMPTY)
            if (date != null) outputDateFormat.format(date) else page.published ?: DATA.EMPTY
        } catch (_: Exception) {
            page.published ?: DATA.EMPTY
        }

        with(holder.binding) {
            title.text = page.title ?: DATA.EMPTY
            description.text = document.text()

            val author = page.authorName ?: DATA.EMPTY
            publishInfo.text = context.getString(R.string.publish_info, author, formattedDate)
        }

        holder.itemView.setOnClickListener {
            VOID.IntentExtra(context, CLASS.BLOGGER_PAGES_DETAILS, "pageId", page.id)
        }
    }

    override fun getItemCount(): Int = pages.size

    class ViewHolder(val binding: ItemBloggerBinding) : RecyclerView.ViewHolder(binding.root)
}