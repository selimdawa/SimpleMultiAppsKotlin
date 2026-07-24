package com.flatcode.simplemultiapps.bloggerapp.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.flatcode.simplemultiapps.bloggerapp.adapter.PagesAdapter
import com.flatcode.simplemultiapps.bloggerapp.model.Page
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.utils.DATA
import com.flatcode.simplemultiapps.databinding.ActivityBloggerPagesBinding
import org.json.JSONObject

class PagesActivity : AppCompatActivity() {

    private var _binding: ActivityBloggerPagesBinding? = null
    private val binding get() = _binding!!

    private val pages = ArrayList<Page>()
    private var adapter: PagesAdapter? = null
    val context: Context = this@PagesActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityBloggerPagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding.toolbar) {
            nameSpace.setText(R.string.blogger_pages)
            back.visibility = View.VISIBLE
            back.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        }

        loadPages()
    }

    private fun loadPages() {
        binding.progressBar.visibility = View.VISIBLE

        val url = "https://googleapis.com{DATA.BLOG_ID}/pages?key=${DATA.BLOGGER_API}"

        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            binding.progressBar.visibility = View.GONE
            try {
                val jsonObject = JSONObject(response ?: DATA.EMPTY)
                val jsonArray = jsonObject.getJSONArray("items")
                pages.clear()

                for (i in 0 until jsonArray.length()) {
                    try {
                        val jsonObject1 = jsonArray.getJSONObject(i)
                        val id = jsonObject1.getString("id")
                        val title = jsonObject1.getString("title")
                        val content = jsonObject1.getString("content")
                        val published = jsonObject1.getString("published")
                        val updated = jsonObject1.getString("updated")
                        val url_ = jsonObject1.getString("url")
                        val selfLink = jsonObject1.getString("selfLink")
                        val displayName = jsonObject1.getJSONObject("author").getString("displayName")

                        val page = Page(
                            displayName, content, id, published,
                            selfLink, title, updated, url_
                        )
                        pages.add(page)
                    } catch (e: Exception) {
                        Toast.makeText(context, e.message ?: DATA.EMPTY, Toast.LENGTH_SHORT).show()
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