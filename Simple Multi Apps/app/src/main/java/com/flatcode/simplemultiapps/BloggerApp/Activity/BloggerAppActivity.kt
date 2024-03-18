package com.flatcode.simplemultiapps.BloggerApp.Activity

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.flatcode.simplemultiapps.BloggerApp.Adapter.PostAdapter
import com.flatcode.simplemultiapps.BloggerApp.Model.Post
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.Unit.CLASS
import com.flatcode.simplemultiapps.Unit.DATA
import com.flatcode.simplemultiapps.Unit.THEME
import com.flatcode.simplemultiapps.Unit.VOID
import com.flatcode.simplemultiapps.databinding.ActivityBloggerAppBinding
import org.json.JSONObject

class BloggerAppActivity : AppCompatActivity() {

    private var binding: ActivityBloggerAppBinding? = null
    private var url = DATA.EMPTY
    private var nextToken = DATA.EMPTY
    private var isSearch = false
    private var posts: ArrayList<Post>? = null
    private var adapter: PostAdapter? = null
    private var dialog: ProgressDialog? = null
    private val context: Context = this@BloggerAppActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityBloggerAppBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        binding!!.toolbar.nameSpace.text = getString(R.string.blogger_name)
        binding!!.toolbar.close.setOnClickListener { onBackPressed() }
        binding!!.toolbar.pages.setOnClickListener { VOID.Intent1(context, CLASS.BLOGGER_PAGES) }

        binding!!.toolbar.search.setOnClickListener {
            binding!!.toolbar.toolbar.visibility = View.GONE
            binding!!.toolbar.toolbarSearch.visibility = View.VISIBLE
            DATA.searchStatus = true
        }

        dialog = ProgressDialog(context)
        dialog!!.setTitle("Please wait...")
        posts = ArrayList()
        posts!!.clear()
        loadPosts()

        binding!!.loadMore.setOnClickListener {
            val query = binding!!.toolbar.textSearch.text.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(query)) {
                loadPosts()
            } else {
                searchPosts(query)
            }
        }
        binding!!.toolbar.postSearch.setOnClickListener {
            nextToken = DATA.EMPTY
            url = DATA.EMPTY
            posts = ArrayList()
            posts!!.clear()
            val query = binding!!.toolbar.textSearch.text.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(query)) {
                loadPosts()
            } else {
                searchPosts(query)
            }
        }
    }

    private fun searchPosts(query: String) {
        isSearch = true
        dialog!!.show()
        url = when (nextToken) {
            DATA.EMPTY -> {
                ("https://www.googleapis.com/blogger/v3/blogs/" + DATA.BLOG_ID + "/posts/search?q="
                        + query + "&key=" + DATA.BLOGGER_API)
            }

            "end" -> {
                Toast.makeText(context, "No more posts...", Toast.LENGTH_SHORT).show()
                dialog!!.dismiss()
                return
            }

            else -> {
                ("https://www.googleapis.com/blogger/v3/blogs/" + DATA.BLOG_ID + "/posts/search?q="
                        + query + "&pageToken=" + nextToken + "&key=" + DATA.BLOGGER_API)
            }
        }
        val stringRequest = StringRequest(Request.Method.GET, url, { response: String? ->
            dialog!!.dismiss()
            try {
                val jsonObject = response?.let { JSONObject(it) }
                nextToken = try {
                    jsonObject!!.getString("nextPageToken")
                } catch (e: Exception) {
                    Toast.makeText(context, "Reached end of page...", Toast.LENGTH_SHORT).show()
                    "end"
                }
                val jsonArray = jsonObject!!.getJSONArray("items")
                for (i in 0 until jsonArray.length()) {
                    try {
                        val jsonObject1 = jsonArray.getJSONObject(i)
                        val id = jsonObject1.getString("id")
                        val title = jsonObject1.getString("title")
                        val content = jsonObject1.getString("content")
                        val published = jsonObject1.getString("published")
                        val updated = jsonObject1.getString("updated")
                        val url = jsonObject1.getString("url")
                        val selfLink = jsonObject1.getString("selfLink")
                        val authorName =
                            jsonObject1.getJSONObject("author").getString("displayName")
                        //String image = jsonObject1.getJSONObject("author").getString("image");
                        val post = Post(
                            DATA.EMPTY + authorName, DATA.EMPTY + content,
                            DATA.EMPTY + id, DATA.EMPTY + published,
                            DATA.EMPTY + selfLink, DATA.EMPTY + title,
                            DATA.EMPTY + updated, DATA.EMPTY + url
                        )
                        posts!!.add(post)
                    } catch (e: Exception) {
                        Toast.makeText(context, DATA.EMPTY + e.message, Toast.LENGTH_SHORT).show()
                    }
                }
                adapter = PostAdapter(context, posts!!)
                binding!!.recyclerView.adapter = adapter
                dialog!!.dismiss()
            } catch (e: Exception) {
                Toast.makeText(context, DATA.EMPTY + e.message, Toast.LENGTH_SHORT).show()
            }
        }) { error: VolleyError ->
            Toast.makeText(context, DATA.EMPTY + error.message, Toast.LENGTH_SHORT).show()
            dialog!!.dismiss()
        }
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }

    private fun loadPosts() {
        isSearch = false
        dialog!!.show()
        url = if (nextToken == DATA.EMPTY) {
            ("https://www.googleapis.com/blogger/v3/blogs/" + DATA.BLOG_ID + "/posts?maxResults="
                    + DATA.MAX_POST_RESULTS + "&key=" + DATA.BLOGGER_API)
        } else if (nextToken == "end") {
            Toast.makeText(context, "No more posts...", Toast.LENGTH_SHORT).show()
            dialog!!.dismiss()
            return
        } else {
            ("https://www.googleapis.com/blogger/v3/blogs/" + DATA.BLOG_ID + "/posts?maxResults="
                    + DATA.MAX_POST_RESULTS + "&pageToken=" + nextToken + "&key=" + DATA.BLOGGER_API)
        }
        val stringRequest = StringRequest(Request.Method.GET, url, { response: String? ->
            dialog!!.dismiss()
            try {
                val jsonObject = response?.let { JSONObject(it) }
                nextToken = try {
                    jsonObject!!.getString("nextPageToken")
                } catch (e: Exception) {
                    Toast.makeText(context, "Reached end of page...", Toast.LENGTH_SHORT).show()
                    "end"
                }
                val jsonArray = jsonObject!!.getJSONArray("items")
                for (i in 0 until jsonArray.length()) {
                    try {
                        val jsonObject1 = jsonArray.getJSONObject(i)
                        val id = jsonObject1.getString("id")
                        val title = jsonObject1.getString("title")
                        val content = jsonObject1.getString("content")
                        val published = jsonObject1.getString("published")
                        val updated = jsonObject1.getString("updated")
                        val url = jsonObject1.getString("url")
                        val selfLink = jsonObject1.getString("selfLink")
                        val authorName =
                            jsonObject1.getJSONObject("author").getString("displayName")
                        val image = jsonObject1.getJSONObject("author").getString("image")
                        val post = Post(
                            DATA.EMPTY + authorName, DATA.EMPTY + content,
                            DATA.EMPTY + id, DATA.EMPTY + published, DATA.EMPTY
                                    + selfLink, DATA.EMPTY + title, DATA.EMPTY + updated,
                            DATA.EMPTY + url
                        )
                        posts!!.add(post)
                    } catch (e: Exception) {
                        Toast.makeText(context, DATA.EMPTY + e.message, Toast.LENGTH_SHORT).show()
                    }
                }
                adapter = PostAdapter(context, posts!!)
                binding!!.recyclerView.adapter = adapter
                dialog!!.dismiss()
            } catch (e: Exception) {
                Toast.makeText(context, DATA.EMPTY + e.message, Toast.LENGTH_SHORT).show()
            }
        }) { error: VolleyError ->
            Toast.makeText(context, DATA.EMPTY + error.message, Toast.LENGTH_SHORT).show()
            dialog!!.dismiss()
        }
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }

    override fun onBackPressed() {
        if (DATA.searchStatus) {
            binding!!.toolbar.toolbar.visibility = View.VISIBLE
            binding!!.toolbar.toolbarSearch.visibility = View.GONE
            DATA.searchStatus = false
            binding!!.toolbar.textSearch.setText(DATA.EMPTY)
        } else super.onBackPressed()
    }
}