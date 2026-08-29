package com.flatcode.simplemultiapps.wordpress.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.wordpress.adapter.WordpressAdapter
import com.flatcode.simplemultiapps.wordpress.model.Post
import com.flatcode.simplemultiapps.wordpress.sqlite.PostDB
import com.flatcode.simplemultiapps.wordpress.utils.WPApiService
import com.flatcode.simplemultiapps.wordpress.utils.WordPressClient
import com.flatcode.simplemultiapps.databinding.ActivityWordpressFavoritesBinding
import com.flatcode.simplemultiapps.wordpress.utils.isNetworkAvailable
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WordpressFavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWordpressFavoritesBinding
    private val context: Context = this
    private var sqLitePostList: List<Post?>? = null
    private var postList: List<Post?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityWordpressFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.toolbar.back.visibility = View.VISIBLE
        binding.toolbar.back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.toolbar.nameSpace.setText(R.string.favorites)

        sqLitePostList = PostDB.getInstance(applicationContext)?.allDbPosts
        setFavListContent(true, sqLitePostList)
    }

    fun setFavListContent(withProgress: Boolean, favPostList: List<Post?>?) {
        if (isNetworkAvailable()) {
            val api: WPApiService = WordPressClient.apiService
            val call: Call<List<Post?>?>? = api.getPosts()

            if (call == null) {
                binding.progressBar.visibility = View.GONE
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
                    val myList = ArrayList<Post>()
                    postList = response.body()

                    val networkPosts = postList?.filterNotNull().orEmpty()
                    val favoriteDbMap = favPostList?.filterNotNull().orEmpty().associateBy { it.wpPostId }

                    for (post in networkPosts) {
                        if (favoriteDbMap.containsKey(post.id)) {
                            myList.add(post)
                        }
                    }

                    binding.recyclerView.adapter = WordpressAdapter(applicationContext, myList)
                }

                override fun onFailure(call: Call<List<Post?>?>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                }
            })
        } else {
            binding.progressBar.visibility = View.GONE
            Snackbar.make(binding.main, R.string.connect_internet, Snackbar.LENGTH_INDEFINITE).show()
        }
    }

    override fun onResume() {
        super.onResume()
        sqLitePostList = PostDB.getInstance(applicationContext)?.allDbPosts
        setFavListContent(true, sqLitePostList)
    }
}