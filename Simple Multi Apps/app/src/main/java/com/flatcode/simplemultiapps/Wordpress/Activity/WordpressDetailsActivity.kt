package com.flatcode.simplemultiapps.Wordpress.Activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.Unit.CLASS
import com.flatcode.simplemultiapps.Unit.DATA
import com.flatcode.simplemultiapps.Unit.THEME
import com.flatcode.simplemultiapps.Wordpress.Model.Media
import com.flatcode.simplemultiapps.Wordpress.Sqlite.PostDB
import com.flatcode.simplemultiapps.Wordpress.Util.InternetConnection
import com.flatcode.simplemultiapps.Wordpress.Util.PageView
import com.flatcode.simplemultiapps.Wordpress.Util.WPApiService
import com.flatcode.simplemultiapps.Wordpress.Util.WordPressClient
import com.flatcode.simplemultiapps.databinding.ActivityWordpressDetailsBinding
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WordpressDetailsActivity : AppCompatActivity() {

    private var _binding: ActivityWordpressDetailsBinding? = null
    private val binding get() = _binding!!

    val context: Context = this@WordpressDetailsActivity
    private var isItemSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        _binding = ActivityWordpressDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getIntExtra("postId", 0)
        val featuredMedia = intent.getIntExtra("featuredMedia", 0)
        val title = intent.getStringExtra("postTitle").orEmpty()
        val contentPost = intent.getStringExtra("postContent").orEmpty()
            .replace("\\\\n".toRegex(), "<br>")
            .replace("\\\\r".toRegex(), "")
            .replace("\\\\".toRegex(), "")

        initToolbar(title, id)
        PageView.initWebView(contentPost, context, binding.content.webview)

        if (InternetConnection.checkInternetConnection(applicationContext)) {
            val api: WPApiService = WordPressClient.apiService
            api.getPostThumbnail(featuredMedia)?.enqueue(object : Callback<Media?> {
                override fun onResponse(call: Call<Media?>, response: Response<Media?>) {
                    if (response.code() != 404) {
                        response.body()?.guid?.get("rendered")?.let { rendered ->
                            val mediaUrl = rendered.toString().replace("\"", "")
                            Glide.with(applicationContext)
                                .load(mediaUrl)
                                .thumbnail(0.5f)
                                .centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(binding.postBackdrop)
                        }
                    }
                }

                override fun onFailure(call: Call<Media?>, t: Throwable) {}
            })
        } else {
            Snackbar.make(binding.root, "Can't connect to the Internet", Snackbar.LENGTH_INDEFINITE).show()
        }

        binding.content.postTitle.text = title
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = intent.getIntExtra("postId", 0)
        val title = intent.getStringExtra("postTitle").orEmpty()
        val excerpt = intent.getStringExtra("postExcerpt").orEmpty()

        if (!isItemSelected) {
            item.icon = ContextCompat.getDrawable(context, R.drawable.ic_heart_selected)
            isItemSelected = true
            PostDB.getInstance(applicationContext)?.insert(id, title, excerpt, isItemSelected)
        } else {
            item.icon = ContextCompat.getDrawable(context, R.drawable.ic_heart_unselected)
            isItemSelected = false
            PostDB.getInstance(applicationContext)?.delete(id)
        }
        return super.onOptionsItemSelected(item)
    }

    private inner class MyWebView : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            view.loadUrl(request.url.toString())
            return true
        }
    }

    private fun initToolbar(title: String, id: Int) {
        window.statusBarColor = Color.TRANSPARENT
        setSupportActionBar(binding.postToolbar)
        binding.postCollapsingToolbarLayout.title = title

        isItemSelected = PostDB.getInstance(applicationContext)?.getDbPostIsFav(id) == true
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.postToolbar.setNavigationOnClickListener { finish() }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.add_to_favorite_menu, menu)
        val favoriteItem = menu.findItem(R.id.add_as_favorite)
        val iconRes = if (isItemSelected) R.drawable.ic_heart_selected else R.drawable.ic_heart_unselected
        favoriteItem.icon = ContextCompat.getDrawable(context, iconRes)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun createIntent(
            context: Context?, id: Int, featuredMedia: Int, title: String?,
            excerpt: String?, content: String?
        ): Intent {
            return Intent(context, CLASS.WORDPRESS_DETAILS).apply {
                putExtra("postId", id)
                putExtra("featuredMedia", featuredMedia)
                putExtra("postExcerpt", excerpt)
                putExtra("postTitle", title)
                putExtra("postContent", content)
            }
        }
    }
}