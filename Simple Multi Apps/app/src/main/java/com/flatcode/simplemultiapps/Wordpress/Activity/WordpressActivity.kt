package com.flatcode.simplemultiapps.wordpress.activity

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.databinding.ActivityWordpressBinding
import com.flatcode.simplemultiapps.utils.THEME
import com.flatcode.simplemultiapps.wordpress.utils.isNetworkAvailable
import com.flatcode.simplemultiapps.utils.launchActivity
import com.flatcode.simplemultiapps.wordpress.adapter.WordpressAdapter
import com.flatcode.simplemultiapps.wordpress.model.Post
import com.flatcode.simplemultiapps.wordpress.utils.WPApiService
import com.flatcode.simplemultiapps.wordpress.utils.WordPressClient
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WordpressActivity : AppCompatActivity() {

    private var _binding: ActivityWordpressBinding? = null
    private val binding get() = _binding!!
    private val context: Context = this
    private var postItemList: List<Post?>? = null
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        _binding = ActivityWordpressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.nameSpace.setText(R.string.wordpress_app)

        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = true
            handler.postDelayed({
                binding.swipeRefresh.isRefreshing = false
                setListContent(false)
            }, 3000)
        }

        binding.toolbar.favorites.setOnClickListener {
            context.launchActivity(WordpressFavoritesActivity::class.java)
        }

        setListContent(true)
    }

    fun setListContent(withProgress: Boolean) {
        if (isNetworkAvailable()) {
            val api: WPApiService = WordPressClient.apiService
            val call: Call<List<Post?>?>? = api.getPosts()

            if (call == null) {
                binding.swipeRefresh.isRefreshing = false
                return
            }

            if (withProgress) {
                binding.progressBar.visibility = View.VISIBLE
            }

            call.enqueue(object : Callback<List<Post?>?> {
                override fun onResponse(
                    call: Call<List<Post?>?>, response: Response<List<Post?>?>
                ) {
                    binding.progressBar.visibility = View.GONE
                    val body = response.body()
                    if (body != null) {
                        postItemList = body
                        val safeList = body.filterNotNull()
                        binding.recyclerView.adapter = WordpressAdapter(context, safeList)
                    }
                }

                override fun onFailure(call: Call<List<Post?>?>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                }
            })
        } else {
            binding.swipeRefresh.isRefreshing = false
            Snackbar.make(
                binding.swipeRefresh, "Can't connect to the Internet", Snackbar.LENGTH_INDEFINITE
            ).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        handler.removeCallbacksAndMessages(null)
    }
}