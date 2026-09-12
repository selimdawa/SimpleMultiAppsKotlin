package com.flatcode.simplemultiapps.bloggerapp.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.bloggerapp.adapter.PagesAdapter
import com.flatcode.simplemultiapps.bloggerapp.model.Page
import com.flatcode.simplemultiapps.databinding.ActivityBloggerPagesBinding
import com.flatcode.simplemultiapps.utils.DATA
import org.jsoup.Jsoup
import org.jsoup.parser.Parser

class PagesActivity : AppCompatActivity() {

    private var _binding: ActivityBloggerPagesBinding? = null
    private val binding get() = _binding!!

    private val pages = ArrayList<Page>()
    private var adapter: PagesAdapter? = null
    val context: Context = this@PagesActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        _binding = ActivityBloggerPagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        with(binding.toolbar) {
            nameSpace.setText(R.string.blogger_pages)
            back.visibility = View.VISIBLE
            back.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        }

        loadPages()
    }

    private fun loadPages() {
        binding.progressBar.visibility = View.VISIBLE

        val url = DATA.PAGES_FEED_URL

        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            binding.progressBar.visibility = View.GONE
            try {
                val doc = Jsoup.parse(response ?: DATA.EMPTY, "", Parser.xmlParser())
                val entries = doc.select("entry")
                pages.clear()

                for (entry in entries) {
                    try {
                        val id = entry.selectFirst("id")?.text()?.split("-")?.last() ?: ""
                        val title = entry.selectFirst("title")?.text() ?: ""
                        val content = entry.selectFirst("content")?.text() ?: ""
                        val published = entry.selectFirst("published")?.text() ?: ""
                        val updated = entry.selectFirst("updated")?.text() ?: ""
                        val urlPath = entry.selectFirst("link[rel=alternate]")?.attr("href") ?: ""
                        val selfLink = entry.selectFirst("link[rel=self]")?.attr("href") ?: ""
                        val authorName = entry.select("author name").first()?.text() ?: DATA.UNKNOWN

                        val page = Page(
                            authorName, content, id, published, selfLink, title, updated, urlPath
                        )
                        pages.add(page)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                adapter = PagesAdapter(context, pages)
                binding.recyclerView.adapter = adapter
            } catch (e: Exception) {
                Toast.makeText(context, e.message ?: DATA.EMPTY, Toast.LENGTH_SHORT).show()
            }
        }) { error ->
            binding.progressBar.visibility = View.GONE
            Toast.makeText(context, error.message ?: DATA.EMPTY, Toast.LENGTH_SHORT).show()
        }

        Volley.newRequestQueue(context).add(stringRequest)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}