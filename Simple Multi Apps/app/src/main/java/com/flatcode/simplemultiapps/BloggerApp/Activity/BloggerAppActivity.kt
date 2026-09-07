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
import com.flatcode.simplemultiapps.bloggerapp.adapter.PostAdapter
import com.flatcode.simplemultiapps.bloggerapp.model.Post
import com.flatcode.simplemultiapps.databinding.ActivityBloggerAppBinding
import com.flatcode.simplemultiapps.utils.DATA
import com.flatcode.simplemultiapps.utils.intent1
import org.json.JSONObject

class BloggerAppActivity : AppCompatActivity() {

    private var _binding: ActivityBloggerAppBinding? = null
    private val binding get() = _binding!!

    private var url = DATA.EMPTY
    private var nextToken = DATA.EMPTY
    private var isSearch = false
    private val posts = ArrayList<Post>()
    private var adapter: PostAdapter? = null
    private val context: Context = this@BloggerAppActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        _binding = ActivityBloggerAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        with(binding.toolbar) {
            nameSpace.text = getString(R.string.blogger_name)
            close.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
            pages.setOnClickListener { context.intent1(PagesActivity::class.java) }
            search.setOnClickListener {
                toolbar.visibility = View.GONE
                toolbarSearch.visibility = View.VISIBLE
                DATA.searchStatus = true
            }
            postSearch.setOnClickListener {
                nextToken = DATA.EMPTY
                url = DATA.EMPTY
                posts.clear()
                val query = textSearch.text.toString().trim()
                if (query.isEmpty()) {
                    loadPosts()
                } else {
                    searchPosts(query)
                }
            }
        }

        loadPosts()

        binding.loadMore.setOnClickListener {
            val query = binding.toolbar.textSearch.text.toString().trim()
            if (query.isEmpty()) {
                loadPosts()
            } else {
                searchPosts(query)
            }
        }
    }

    private fun searchPosts(query: String) {
        isSearch = true
        binding.progressBar.visibility = View.VISIBLE

        url = when (nextToken) {
            DATA.EMPTY -> {
                "${DATA.BLOGGER_BASE_URL}${DATA.BLOG_ID}/${DATA.POSTS}/search?q=$query&key=${DATA.BLOGGER_API}"
            }

            DATA.END -> {
                Toast.makeText(context, R.string.no_more_posts, Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
                return
            }

            else -> {
                "${DATA.BLOGGER_BASE_URL}${DATA.BLOG_ID}/${DATA.POSTS}/search?q=$query&pageToken=$nextToken&key=${DATA.BLOGGER_API}"
            }
        }

        val stringRequest = StringRequest(
            Request.Method.GET, url, { response ->
            binding.progressBar.visibility = View.GONE
            if (response.isNullOrEmpty()) return@StringRequest
            try {
                val jsonObject = JSONObject(response)
                nextToken = try {
                    jsonObject.getString(DATA.NEXT_PAGE_TOKEN)
                } catch (_: Exception) {
                    Toast.makeText(context, R.string.reached_end_of_page, Toast.LENGTH_SHORT).show()
                    DATA.END
                }

                val jsonArray = jsonObject.getJSONArray(DATA.ITEMS)
                for (i in 0 until jsonArray.length()) {
                    try {
                        val jsonObject1 = jsonArray.getJSONObject(i)
                        val id = jsonObject1.getString(DATA.ID)
                        val title = jsonObject1.getString(DATA.TITLE)
                        val content = jsonObject1.getString(DATA.CONTENT)
                        val published = jsonObject1.getString(DATA.PUBLISHED)
                        val updated = jsonObject1.getString(DATA.UPDATED)
                        val urlPath = jsonObject1.getString(DATA.URL)
                        val selfLink = jsonObject1.getString(DATA.SELF_LINK)
                        val authorName =
                            jsonObject1.getJSONObject(DATA.AUTHOR).getString(DATA.DISPLAY_NAME)

                        posts.add(
                            Post(
                                authorName,
                                content,
                                id,
                                published,
                                selfLink,
                                title,
                                updated,
                                urlPath,
                            ),
                        )
                    } catch (e: Exception) {
                        Toast.makeText(context, e.message ?: DATA.EMPTY, Toast.LENGTH_SHORT).show()
                    }
                }
                adapter = PostAdapter(context, posts)
                binding.recyclerView.adapter = adapter
            } catch (e: Exception) {
                Toast.makeText(context, e.message ?: DATA.EMPTY, Toast.LENGTH_SHORT).show()
            }
        },
            { error ->
                Toast.makeText(context, error.message ?: DATA.EMPTY, Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            },
        )

        Volley.newRequestQueue(context).add(stringRequest)
    }

    private fun loadPosts() {
        isSearch = false
        binding.progressBar.visibility = View.VISIBLE

        url = when (nextToken) {
            DATA.EMPTY -> {
                "${DATA.BLOGGER_BASE_URL}${DATA.BLOG_ID}/${DATA.POSTS}?maxResults=${DATA.MAX_POST_RESULTS}&key=${DATA.BLOGGER_API}"
            }

            DATA.END -> {
                Toast.makeText(context, R.string.no_more_posts, Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
                return
            }

            else -> {
                "${DATA.BLOGGER_BASE_URL}${DATA.BLOG_ID}/${DATA.POSTS}?maxResults=${DATA.MAX_POST_RESULTS}&pageToken=$nextToken&key=${DATA.BLOGGER_API}"
            }
        }

        val stringRequest = StringRequest(
            Request.Method.GET, url, { response ->
            binding.progressBar.visibility = View.GONE
            if (response.isNullOrEmpty()) return@StringRequest
            try {
                val jsonObject = JSONObject(response)
                nextToken = try {
                    jsonObject.getString(DATA.NEXT_PAGE_TOKEN)
                } catch (_: Exception) {
                    Toast.makeText(context, R.string.reached_end_of_page, Toast.LENGTH_SHORT).show()
                    DATA.END
                }

                val jsonArray = jsonObject.getJSONArray(DATA.ITEMS)
                for (i in 0 until jsonArray.length()) {
                    try {
                        val jsonObject1 = jsonArray.getJSONObject(i)
                        val id = jsonObject1.getString(DATA.ID)
                        val title = jsonObject1.getString(DATA.TITLE)
                        val content = jsonObject1.getString(DATA.CONTENT)
                        val published = jsonObject1.getString(DATA.PUBLISHED)
                        val updated = jsonObject1.getString(DATA.UPDATED)
                        val urlPath = jsonObject1.getString(DATA.URL)
                        val selfLink = jsonObject1.getString(DATA.SELF_LINK)
                        val authorName =
                            jsonObject1.getJSONObject(DATA.AUTHOR).getString(DATA.DISPLAY_NAME)

                        posts.add(
                            Post(
                                authorName,
                                content,
                                id,
                                published,
                                selfLink,
                                title,
                                updated,
                                urlPath,
                            ),
                        )
                    } catch (e: Exception) {
                        Toast.makeText(context, e.message ?: DATA.EMPTY, Toast.LENGTH_SHORT).show()
                    }
                }
                adapter = PostAdapter(context, posts)
                binding.recyclerView.adapter = adapter
            } catch (e: Exception) {
                Toast.makeText(context, e.message ?: DATA.EMPTY, Toast.LENGTH_SHORT).show()
            }
        },
            { error ->
                Toast.makeText(context, error.message ?: DATA.EMPTY, Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            },
        )

        Volley.newRequestQueue(context).add(stringRequest)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}