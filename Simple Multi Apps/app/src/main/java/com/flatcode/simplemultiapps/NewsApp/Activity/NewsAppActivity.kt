package com.flatcode.simplemultiapps.NewsApp.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.flatcode.simplemultiapps.NewsApp.Adapter.NewsAppAdapter
import com.flatcode.simplemultiapps.NewsApp.Model.NewsApiResponse
import com.flatcode.simplemultiapps.NewsApp.Model.NewsHeadlines
import com.flatcode.simplemultiapps.NewsApp.OnFetchDataListener
import com.flatcode.simplemultiapps.NewsApp.RequestManger
import com.flatcode.simplemultiapps.NewsApp.SelectListener
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.Unit.DATA
import com.flatcode.simplemultiapps.Unit.THEME
import com.flatcode.simplemultiapps.databinding.ActivityNewsAppBinding

class NewsAppActivity : AppCompatActivity(), SelectListener, View.OnClickListener {

    private lateinit var binding: ActivityNewsAppBinding
    private val context: Context = this@NewsAppActivity
    private var adapter: NewsAppAdapter? = null
    private var progressDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityNewsAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })

        binding.toolbar.back.visibility = View.VISIBLE
        binding.toolbar.back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.toolbar.nameSpace.setText(R.string.news_app)

        setupProgressDialog()

        binding.search.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                updateProgressDialog("Fetching news Articles of $query")
                val manger = RequestManger(context)
                manger.getNewsHeadlines(listener, "general", query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        binding.linearSwitchUser.business.setOnClickListener(this)
        binding.linearSwitchUser.entertainment.setOnClickListener(this)
        binding.linearSwitchUser.general.setOnClickListener(this)
        binding.linearSwitchUser.health.setOnClickListener(this)
        binding.linearSwitchUser.science.setOnClickListener(this)
        binding.linearSwitchUser.sports.setOnClickListener(this)
        binding.linearSwitchUser.technology.setOnClickListener(this)

        val manger = RequestManger(context)
        manger.getNewsHeadlines(listener, "general", null)
    }

    private val listener: OnFetchDataListener<NewsApiResponse> =
        object : OnFetchDataListener<NewsApiResponse> {
            override fun onFetchData(list: List<NewsHeadlines?>?, message: String?) {
                progressDialog?.dismiss()
                if (list.isNullOrEmpty()) {
                    Toast.makeText(context, "No data found! ", Toast.LENGTH_SHORT).show()
                } else {
                    showNews(list)
                }
            }

            override fun onError(message: String?) {
                progressDialog?.dismiss()
                Toast.makeText(context, "Error! ", Toast.LENGTH_SHORT).show()
            }
        }

    private fun showNews(list: List<NewsHeadlines?>?) {
        adapter = NewsAppAdapter(context, list, this)
        binding.recyclerView.adapter = adapter
    }

    override fun onNewsClicked(headlines: NewsHeadlines?) {
        if (headlines != null) {
            val intent = Intent(context, NewsAppDetailsActivity::class.java).apply {
                putExtra(DATA.DATA, headlines)
            }
            startActivity(intent)
        } else {
            Toast.makeText(context, "News details are unavailable", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClick(view: View) {
        val button = view as TextView
        val category = button.text.toString()
        updateProgressDialog("Fetching news Articles of $category")
        val manger = RequestManger(context)
        manger.getNewsHeadlines(listener, category, null)
    }

    private fun setupProgressDialog() {
        progressDialog = AlertDialog.Builder(context)
            .setMessage("Fetching news Articles...")
            .setCancelable(false)
            .create()
        progressDialog?.show()
    }

    private fun updateProgressDialog(message: String) {
        progressDialog?.setMessage(message)
        progressDialog?.show()
    }
}