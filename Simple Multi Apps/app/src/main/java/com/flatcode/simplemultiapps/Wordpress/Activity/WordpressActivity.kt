package com.flatcode.simplemultiapps.wordpress.activity

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.databinding.ActivityWordpressBinding
import com.flatcode.simplemultiapps.wordpress.utils.isNetworkAvailable
import com.flatcode.simplemultiapps.utils.intent1
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
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        _binding = ActivityWordpressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.toolbar.nameSpace.setText(R.string.wordpress_app)

        binding.main.setOnRefreshListener {
            binding.main.isRefreshing = true
            handler.postDelayed({
                binding.main.isRefreshing = false
                setListContent(false)
            }, 3000)
        }

        binding.toolbar.favorites.setOnClickListener {
            context.intent1(WordpressFavoritesActivity::class.java)
        }

        setListContent(true)
    }

    fun setListContent(withProgress: Boolean) {
        if (isNetworkAvailable()) {
            val api: WPApiService = WordPressClient.apiService
            val call: Call<List<Post?>?>? = api.getPosts()

            if (call == null) {
                binding.main.isRefreshing = false
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
            binding.main.isRefreshing = false
            Snackbar.make(
                binding.main, R.string.connect_internet, Snackbar.LENGTH_INDEFINITE
            ).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        handler.removeCallbacksAndMessages(null)
    }
}