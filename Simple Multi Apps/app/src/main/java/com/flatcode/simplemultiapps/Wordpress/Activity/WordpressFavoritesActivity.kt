package com.flatcode.simplemultiapps.Wordpress.Activity

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.Unit.THEME
import com.flatcode.simplemultiapps.Wordpress.Adapter.WordpressAdapter
import com.flatcode.simplemultiapps.Wordpress.Model.Post
import com.flatcode.simplemultiapps.Wordpress.Sqlite.PostDB
import com.flatcode.simplemultiapps.Wordpress.Util.InternetConnection
import com.flatcode.simplemultiapps.Wordpress.Util.WPApiService
import com.flatcode.simplemultiapps.Wordpress.Util.WordPressClient
import com.flatcode.simplemultiapps.databinding.ActivityWordpressFavoritesBinding
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WordpressFavoritesActivity : AppCompatActivity() {

    private var _binding: ActivityWordpressFavoritesBinding? = null
    private val binding get() = _binding!!

    val context: Context = this@WordpressFavoritesActivity
    private var sqLitePostList: List<Post?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        _binding = ActivityWordpressFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding.toolbar) {
            back.visibility = View.VISIBLE
            back.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
            nameSpace.setText(R.string.favorites)
        }

        sqLitePostList = PostDB.getInstance(applicationContext)?.allDbPosts
        setFavListContent(withProgress = true, favPostList = sqLitePostList)
    }

    fun setFavListContent(withProgress: Boolean, favPostList: List<Post?>?) {
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

                    val serverPosts = response.body()?.filterNotNull().orEmpty()
                    val favoriteIdsSet = favPostList?.filterNotNull()?.map { it.wpPostId }?.toSet().orEmpty()

                    val matchedFavoritesList = serverPosts.filter { post ->
                        post.id in favoriteIdsSet
                    }

                    binding.recyclerView.adapter = WordpressAdapter(context, matchedFavoritesList)
                }

                override fun onFailure(call: Call<List<Post?>?>, t: Throwable) {
                    if (withProgress) {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            })
        } else {
            Snackbar.make(binding.root, "Can't connect to the Internet", Snackbar.LENGTH_INDEFINITE).show()
        }
    }

    override fun onResume() {
        super.onResume()
        sqLitePostList = PostDB.getInstance(applicationContext)?.allDbPosts
        setFavListContent(withProgress = true, favPostList = sqLitePostList)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}