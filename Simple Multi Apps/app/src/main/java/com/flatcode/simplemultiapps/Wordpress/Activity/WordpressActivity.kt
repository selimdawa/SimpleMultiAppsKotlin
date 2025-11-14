package com.flatcode.simplemultiapps.Wordpress.Activity

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
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

    private var binding: ActivityWordpressBinding? = null
    var context: Context = this@WordpressActivity
    private var postItemList: List<Post?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityWordpressBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        binding!!.toolbar.nameSpace.setText(R.string.wordpress_app)
        binding!!.swipeRefresh.setOnRefreshListener {
            binding!!.swipeRefresh.isRefreshing = true
            Handler().postDelayed({
                binding!!.swipeRefresh.isRefreshing = false
                setListContent(false)
            }, 3000)
        }
        binding!!.toolbar.favorites.setOnClickListener {
            VOID.Intent1(context, CLASS.WORDPRESS_FAVORITES)
        }
        setListContent(true)
    }

    fun setListContent(withProgress: Boolean) {
        if (InternetConnection.checkInternetConnection(applicationContext)) {
            val api: WPApiService = WordPressClient.apiService
            val call: Call<List<Post?>?> = api.getPosts()!!
            val progressDialog: ProgressDialog

            progressDialog = ProgressDialog(this)
            progressDialog.setTitle(getString(R.string.progressdialog_title))
            progressDialog.setMessage(getString(R.string.progressdialog_message))
            if (withProgress) {
                progressDialog.show()
            }

            call.enqueue(object : Callback<List<Post?>?> {
                override fun onResponse(
                    call: Call<List<Post?>?>, response: Response<List<Post?>?>
                ) {
                    postItemList = response.body()
                    //binding.recyclerView.setHasFixedSize(true);
                    binding!!.recyclerView.adapter = WordpressAdapter(
                        context,
                        postItemList as List<Post>
                    )
                    if (withProgress) {
                        progressDialog.dismiss()
                    }
                }

                override fun onFailure(call: Call<List<Post?>?>, t: Throwable) {
                    if (withProgress) {
                        progressDialog.dismiss()
                    }
                }
            })
        } else {
            Snackbar.make(
                binding!!.swipeRefresh, "Can't connect to the Internet", Snackbar.LENGTH_INDEFINITE
            ).show()
        }
    }
}