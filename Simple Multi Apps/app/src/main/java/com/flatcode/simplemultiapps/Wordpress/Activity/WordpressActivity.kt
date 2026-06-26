package com.flatcode.simplemultiapps.Wordpress.Activity

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.Unit.CLASS
import com.flatcode.simplemultiapps.Unit.THEME
import com.flatcode.simplemultiapps.Unit.VOID
import com.flatcode.simplemultiapps.Wordpress.Adapter.WordpressAdapter
import com.flatcode.simplemultiapps.Wordpress.Model.Post
import com.flatcode.simplemultiapps.Wordpress.Util.InternetConnection
import com.flatcode.simplemultiapps.Wordpress.Util.WPApiService
import com.flatcode.simplemultiapps.Wordpress.Util.WordPressClient
import com.flatcode.simplemultiapps.databinding.ActivityWordpressBinding
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WordpressActivity : AppCompatActivity() {

    private var _binding: ActivityWordpressBinding? = null
    private val binding get() = _binding!!

    val context: Context = this@WordpressActivity
    private var postItemList: List<Post?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        _binding = ActivityWordpressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.nameSpace.setText(R.string.wordpress_app)

        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = true
            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipeRefresh.isRefreshing = false
                setListContent(withProgress = false)
            }, 3000)
        }

        binding.toolbar.favorites.setOnClickListener {
            VOID.Intent1(context, CLASS.WORDPRESS_FAVORITES)
        }

        setListContent(withProgress = true)
    }

    fun setListContent(withProgress: Boolean) {
        if (InternetConnection.checkInternetConnection(applicationContext)) {
            val api: WPApiService = WordPressClient.apiService
            val call: Call<List<Post?>?>? = api.getPosts()

            if (withProgress) {
                binding.progressBar.visibility = View.VISIBLE
            }

            call?.enqueue(object : Callback<List<Post?>?> {
                override fun onResponse(
                    call: Call<List<Post?>?>, response: Response<List<Post?>?>
                ) {
                    if (withProgress) {
                        binding.progressBar.visibility = View.GONE
                    }

                    postItemList = response.body()
                    val secureList = (postItemList?.filterNotNull() ?: emptyList())

                    binding.recyclerView.adapter = WordpressAdapter(context, secureList)
                }

                override fun onFailure(call: Call<List<Post?>?>, t: Throwable) {
                    if (withProgress) {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            })
        } else {
            Snackbar.make(
                binding.swipeRefresh, "Can't connect to the Internet", Snackbar.LENGTH_INDEFINITE
            ).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}