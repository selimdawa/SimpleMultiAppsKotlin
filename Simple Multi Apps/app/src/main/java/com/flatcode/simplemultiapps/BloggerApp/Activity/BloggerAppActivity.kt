package com.flatcode.simplemultiapps.bloggerapp.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.flatcode.simplemultiapps.bloggerapp.adapter.PostAdapter
import com.flatcode.simplemultiapps.bloggerapp.model.Post
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.utils.CLASS
import com.flatcode.simplemultiapps.utils.DATA
import com.flatcode.simplemultiapps.utils.THEME
import com.flatcode.simplemultiapps.utils.VOID
import com.flatcode.simplemultiapps.databinding.ActivityBloggerAppBinding
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
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        _binding = ActivityBloggerAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding.toolbar) {
            nameSpace.text = getString(R.string.blogger_name)
            close.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
            pages.setOnClickListener { VOID.Intent1(context, CLASS.BLOGGER_PAGES) }
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
                "https://googleapis.com{DATA.BLOG_ID}/posts/search?q=$query&key=${DATA.BLOGGER_API}"
            }

            "end" -> {
                Toast.makeText(context, "No more posts...", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
                return
            }

            else -> {
                "https://googleapis.com{DATA.BLOG_ID}/posts/search?q=$query&pageToken=$nextToken&key=${DATA.BLOGGER_API}"
            }
        }

        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            binding.progressBar.visibility = View.GONE
            try {
                val jsonObject = JSONObject(response ?: DATA.EMPTY)
                nextToken = try {
                    jsonObject.getString("nextPageToken")
                } catch (_: Exception) {
                    Toast.makeText(context, "Reached end of page...", Toast.LENGTH_SHORT).show()
                    "end"
                }

                val jsonArray = jsonObject.getJSONArray("items")
                for (i in 0 until jsonArray.length()) {
                    try {
                        val jsonObject1 = jsonArray.getJSONObject(i)
                        val id = jsonObject1.getString("id")
                        val title = jsonObject1.getString("title")
                        val content = jsonObject1.getString("content")
                        val published = jsonObject1.getString("published")
                        val updated = jsonObject1.getString("updated")
                        val urlPath = jsonObject1.getString("url")
                        val selfLink = jsonObject1.getString("selfLink")
                        val authorName =
                            jsonObject1.getJSONObject("author").getString("displayName")

                        posts.add(
                            Post(
                                authorName,
                                content,
                                id,
                                published,
                                selfLink,
                                title,
                                updated,
                                urlPath
                            )
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
        }) { error ->
            Toast.makeText(context, error.message ?: DATA.EMPTY, Toast.LENGTH_SHORT).show()
            binding.progressBar.visibility = View.GONE
        }

        Volley.newRequestQueue(context).add(stringRequest)
    }

    private fun loadPosts() {
        isSearch = false
        binding.progressBar.visibility = View.VISIBLE

        url = when (nextToken) {
            DATA.EMPTY -> {
                "https://googleapis.com{DATA.BLOG_ID}/posts?maxResults=${DATA.MAX_POST_RESULTS}&key=${DATA.BLOGGER_API}"
            }

            "end" -> {
                Toast.makeText(context, "No more posts...", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
                return
            }

            else -> {
                "https://googleapis.com{DATA.BLOG_ID}/posts?maxResults=${DATA.MAX_POST_RESULTS}&pageToken=$nextToken&key=${DATA.BLOGGER_API}"
            }
        }

        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            binding.progressBar.visibility = View.GONE
            try {
                val jsonObject = JSONObject(response ?: DATA.EMPTY)
                nextToken = try {
                    jsonObject.getString("nextPageToken")
                } catch (_: Exception) {
                    Toast.makeText(context, "Reached end of page...", Toast.LENGTH_SHORT).show()
                    "end"
                }

                val jsonArray = jsonObject.getJSONArray("items")
                for (i in 0 until jsonArray.length()) {
                    try {
                        val jsonObject1 = jsonArray.getJSONObject(i)
                        val id = jsonObject1.getString("id")
                        val title = jsonObject1.getString("title")
                        val content = jsonObject1.getString("content")
                        val published = jsonObject1.getString("published")
                        val updated = jsonObject1.getString("updated")
                        val urlPath = jsonObject1.getString("url")
                        val selfLink = jsonObject1.getString("selfLink")
                        val authorName =
                            jsonObject1.getJSONObject("author").getString("displayName")

                        posts.add(
                            Post(
                                authorName,
                                content,
                                id,
                                published,
                                selfLink,
                                title,
                                updated,
                                urlPath
                            )
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
        }) { error ->
            Toast.makeText(context, error.message ?: DATA.EMPTY, Toast.LENGTH_SHORT).show()
            binding.progressBar.visibility = View.GONE
        }

        Volley.newRequestQueue(context).add(stringRequest)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}