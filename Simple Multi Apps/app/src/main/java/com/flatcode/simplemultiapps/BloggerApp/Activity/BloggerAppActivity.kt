package com.flatcode.simplemultiapps.bloggerapp.activity

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
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
import com.flatcode.simplemultiapps.utils.openActivity
import org.jsoup.Jsoup
import org.jsoup.parser.Parser

class BloggerAppActivity : AppCompatActivity() {

    private var _binding: ActivityBloggerAppBinding? = null
    private val binding get() = _binding!!

    private var url = DATA.EMPTY
    private var nextToken = "1"
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
            close.setOnClickListener { resetSearch() }
            pages.setOnClickListener { context.openActivity(PagesActivity::class.java) }
            search.setOnClickListener {
                toolbar.visibility = View.GONE
                toolbarSearch.visibility = View.VISIBLE
                DATA.searchStatus = true
            }
            postSearch.visibility = View.GONE

            textSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val query = s.toString().trim()
                    adapter?.filter(query)
                    val isEmpty = adapter?.itemCount == 0
                    binding.noResultsText.visibility =
                        if (isEmpty && query.isNotEmpty()) View.VISIBLE else View.GONE
                    binding.loadMoreLayout.visibility =
                        if (query.isEmpty() && nextToken != DATA.END) View.VISIBLE else View.GONE
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (DATA.searchStatus) {
                    resetSearch()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })

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

    private fun resetSearch() {
        binding.toolbar.toolbar.visibility = View.VISIBLE
        binding.toolbar.toolbarSearch.visibility = View.GONE
        binding.toolbar.textSearch.setText("")
        DATA.searchStatus = false
        adapter?.filter("")
        binding.noResultsText.visibility = View.GONE
        binding.loadMoreLayout.visibility = if (nextToken == DATA.END) View.GONE else View.VISIBLE
    }

    private fun searchPosts(query: String) {
        val isInitial = nextToken == "1" || nextToken == DATA.EMPTY
        isSearch = true
        setLoading(true, isInitial)

        url = when (nextToken) {
            DATA.END -> {
                Toast.makeText(context, "No more posts...", Toast.LENGTH_SHORT).show()
                setLoading(false, isInitial)
                return
            }

            else -> {
                "${DATA.FEED_URL}?q=$query&start-index=$nextToken&max-results=${DATA.MAX_POST_RESULTS}"
            }
        }

        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            setLoading(false, isInitial)
            try {
                val doc = Jsoup.parse(response ?: DATA.EMPTY, "", Parser.xmlParser())
                val entries = doc.select("entry")

                if (entries.isEmpty()) {
                    Toast.makeText(context, "No posts found...", Toast.LENGTH_SHORT).show()
                    nextToken = DATA.END
                    setLoading(false, isInitial)
                    return@StringRequest
                }

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
                        e.printStackTrace()
                    }
                }

                nextToken = if (entries.size < DATA.MAX_POST_RESULTS.toInt()) {
                    DATA.END
                } else {
                    (nextToken.toInt() + entries.size).toString()
                }

                if (adapter == null) {
                    adapter = PostAdapter(context, ArrayList(posts))
                    binding.recyclerView.adapter = adapter
                } else {
                    adapter?.updateList(ArrayList(posts))
                }

                val queryText = binding.toolbar.textSearch.text.toString().trim()
                if (queryText.isNotEmpty()) {
                    adapter?.filter(queryText)
                }

                setLoading(false, isInitial)
                binding.recyclerView.requestLayout()
            } catch (e: Exception) {
                Toast.makeText(context, e.message ?: DATA.EMPTY, Toast.LENGTH_SHORT).show()
            }
        }) { error ->
            Toast.makeText(context, error.message ?: DATA.EMPTY, Toast.LENGTH_SHORT).show()
            setLoading(false, isInitial)
        }

        Volley.newRequestQueue(context).add(stringRequest)
    }

    private fun loadPosts() {
        val isInitial = nextToken == "1" || nextToken == DATA.EMPTY
        isSearch = false
        setLoading(true, isInitial)

        url = when (nextToken) {
            DATA.END -> {
                Toast.makeText(context, "No more posts...", Toast.LENGTH_SHORT).show()
                setLoading(false, isInitial)
                return
            }

            else -> {
                "${DATA.FEED_URL}?start-index=$nextToken&max-results=${DATA.MAX_POST_RESULTS}"
            }
        }

        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            setLoading(false, isInitial)
            try {
                val doc = Jsoup.parse(response ?: DATA.EMPTY, "", Parser.xmlParser())
                val entries = doc.select("entry")

                if (entries.isEmpty()) {
                    nextToken = DATA.END
                    setLoading(false, isInitial)
                    return@StringRequest
                }

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
                        e.printStackTrace()
                    }
                }

                nextToken = if (entries.size < DATA.MAX_POST_RESULTS.toInt()) {
                    DATA.END
                } else {
                    (nextToken.toInt() + entries.size).toString()
                }

                if (adapter == null) {
                    adapter = PostAdapter(context, ArrayList(posts))
                    binding.recyclerView.adapter = adapter
                } else {
                    adapter?.updateList(ArrayList(posts))
                }

                val queryText = binding.toolbar.textSearch.text.toString().trim()
                if (queryText.isNotEmpty()) {
                    adapter?.filter(queryText)
                }

                setLoading(false, isInitial)
                binding.recyclerView.requestLayout()
            } catch (e: Exception) {
                Toast.makeText(context, e.message ?: DATA.EMPTY, Toast.LENGTH_SHORT).show()
            }
        }) { error ->
            Toast.makeText(context, error.message ?: DATA.EMPTY, Toast.LENGTH_SHORT).show()
            setLoading(false, isInitial)
        }

        Volley.newRequestQueue(context).add(stringRequest)
    }

    private fun setLoading(isLoading: Boolean, isInitial: Boolean) {
        if (isLoading) {
            if (isInitial) {
                binding.progressBar.visibility = View.VISIBLE
                binding.loadMoreLayout.visibility = View.GONE
            } else {
                binding.loadMore.visibility = View.GONE
                binding.loadMoreProgress.visibility = View.VISIBLE
            }
        } else {
            binding.progressBar.visibility = View.GONE
            binding.loadMoreProgress.visibility = View.GONE
            if (nextToken == DATA.END) {
                binding.loadMoreLayout.visibility = View.GONE
            } else {
                binding.loadMoreLayout.visibility = View.VISIBLE
                binding.loadMore.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}