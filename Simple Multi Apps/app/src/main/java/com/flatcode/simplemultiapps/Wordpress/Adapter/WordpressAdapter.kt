package com.flatcode.simplemultiapps.Wordpress.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.Wordpress.Activity.WordpressDetailsActivity
import com.flatcode.simplemultiapps.Wordpress.Model.Post
import com.flatcode.simplemultiapps.databinding.ItemWordpressBinding

class WordpressAdapter(
    private val context: Context,
    private val posts: List<Post>
) : RecyclerView.Adapter<WordpressAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemWordpressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int = posts.size

    inner class PostViewHolder(val binding: ItemWordpressBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) {
            val title = post.title?.get("rendered").toString().replace("\"", "")
            val excerpt = post.excerpt?.get("rendered").toString().replace("\"", "")

            binding.title.text = HtmlCompat.fromHtml(title, HtmlCompat.FROM_HTML_MODE_LEGACY)
            binding.content.text = HtmlCompat.fromHtml(excerpt, HtmlCompat.FROM_HTML_MODE_LEGACY)

            binding.root.setOnClickListener { view ->
                val adapterPos = bindingAdapterPosition
                if (adapterPos == RecyclerView.NO_POSITION) return@setOnClickListener

                val currentPost = posts[adapterPos]
                val postTitle = currentPost.title?.get("rendered").toString().replace("\"", "")
                val postExcerpt = currentPost.excerpt?.get("rendered").toString().replace("\"", "")
                var postContent = currentPost.content?.get("rendered").toString().replace("\"", "")

                postContent = contentFilter(postContent, "<ins", "</ins>")
                postContent = videoFilter(postContent, "<iframe", "/iframe>")

                val formattedTitle = HtmlCompat.fromHtml(postTitle, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

                val intent: Intent = WordpressDetailsActivity.createIntent(
                    view.context,
                    currentPost.id,
                    currentPost.featured_media,
                    formattedTitle,
                    postExcerpt,
                    postContent
                )
                view.context.startActivity(intent)
            }
        }

        private fun contentFilter(content: String, first: String, last: String): String {
            val firstIndex = content.indexOf(first)
            val lastIndex = content.lastIndexOf(last)
            if (firstIndex != -1 && lastIndex != -1 && lastIndex > firstIndex) {
                val substringToRemove = content.substring(firstIndex, lastIndex + last.length)
                return content.replace(substringToRemove, "")
            }
            return content
        }

        private fun videoFilter(content: String, first: String, last: String): String {
            val firstIndex = content.indexOf(first)
            val lastIndex = content.lastIndexOf(last)
            if (firstIndex != -1 && lastIndex != -1 && lastIndex > firstIndex) {
                val oldContentSubstring = content.substring(firstIndex, lastIndex + last.length)
                val newContentSubstring = "<div class=\"videoWrapper\">$oldContentSubstring</div>"
                return content.replace(oldContentSubstring, newContentSubstring)
            }
            return content
        }
    }
}