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
    private val context: Context = this@PagesActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        _binding = ActivityBloggerPagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
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

        val url = "${DATA.BLOGGER_BASE_URL}${DATA.BLOG_ID}/${DATA.PAGES}?key=${DATA.BLOGGER_API}"

        val stringRequest = StringRequest(
            Request.Method.GET, url, { response ->
            binding.progressBar.visibility = View.GONE
            if (response.isNullOrEmpty()) return@StringRequest
            try {
                val jsonObject = JSONObject(response)
                val jsonArray = jsonObject.getJSONArray(DATA.ITEMS)
                pages.clear()

                for (i in 0 until jsonArray.length()) {
                    try {
                        val jsonObject1 = jsonArray.getJSONObject(i)
                        val id = jsonObject1.getString(DATA.ID)
                        val title = jsonObject1.getString(DATA.TITLE)
                        val content = jsonObject1.getString(DATA.CONTENT)
                        val published = jsonObject1.getString(DATA.PUBLISHED)
                        val updated = jsonObject1.getString(DATA.UPDATED)
                        val pageUrl = jsonObject1.getString(DATA.URL)
                        val selfLink = jsonObject1.getString(DATA.SELF_LINK)
                        val displayName =
                            jsonObject1.getJSONObject(DATA.AUTHOR).getString(DATA.DISPLAY_NAME)

                        val page = Page(
                            displayName,
                            content,
                            id,
                            published,
                            selfLink,
                            title,
                            updated,
                            pageUrl,
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
        },
            { error ->
                binding.progressBar.visibility = View.GONE
                Toast.makeText(context, error.message ?: DATA.EMPTY, Toast.LENGTH_SHORT).show()
            },
        )

        Volley.newRequestQueue(context).add(stringRequest)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}