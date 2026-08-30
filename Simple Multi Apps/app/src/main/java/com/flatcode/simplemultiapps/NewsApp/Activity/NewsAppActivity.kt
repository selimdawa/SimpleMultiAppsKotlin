package com.flatcode.simplemultiapps.newsapp.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.flatcode.simplemultiapps.newsapp.OnFetchDataListener
import com.flatcode.simplemultiapps.newsapp.RequestManager
import com.flatcode.simplemultiapps.newsapp.SelectListener
import com.flatcode.simplemultiapps.newsapp.adapter.NewsAppAdapter
import com.flatcode.simplemultiapps.newsapp.model.NewsApiResponse
import com.flatcode.simplemultiapps.newsapp.model.NewsHeadlines
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.utils.DATA
import com.flatcode.simplemultiapps.databinding.ActivityNewsAppBinding

class NewsAppActivity : AppCompatActivity(), SelectListener, View.OnClickListener {

    private var _binding: ActivityNewsAppBinding? = null
    private val binding get() = _binding!!

    private val context: Context = this@NewsAppActivity
    private var adapter: NewsAppAdapter? = null
    private var progressDialog: AlertDialog? = null

    private val listener = object : OnFetchDataListener<NewsApiResponse> {
        override fun onFetchData(list: List<NewsHeadlines?>?, message: String?) {
            progressDialog?.dismiss()
            if (list.isNullOrEmpty()) {
                Toast.makeText(context, R.string.no_data_found, Toast.LENGTH_SHORT).show()
            } else {
                showNews(list)
            }
        }

        override fun onError(message: String?) {
            progressDialog?.dismiss()
            Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        _binding = ActivityNewsAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(enabled = true) {
                override fun handleOnBackPressed() {
                    finish()
                }
            },
        )

        with(binding.toolbar) {
            back.visibility = View.VISIBLE
            back.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
            nameSpace.setText(R.string.news_app)
        }

        setupProgressDialog()

        binding.search.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                updateProgressDialog(getString(R.string.fetching_news_of, query))
                RequestManager(context).getNewsHeadlines(listener, "general", query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean = false
        })

        with(binding.linearSwitchUser) {
            business.setOnClickListener(this@NewsAppActivity)
            entertainment.setOnClickListener(this@NewsAppActivity)
            general.setOnClickListener(this@NewsAppActivity)
            health.setOnClickListener(this@NewsAppActivity)
            science.setOnClickListener(this@NewsAppActivity)
            sports.setOnClickListener(this@NewsAppActivity)
            technology.setOnClickListener(this@NewsAppActivity)
        }

        RequestManager(context).getNewsHeadlines(listener, "general", null)
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
            Toast.makeText(context, R.string.news_details_unavailable, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClick(view: View) {
        val button = view as TextView
        val category = button.text.toString()
        updateProgressDialog(getString(R.string.fetching_news_of, category))
        RequestManager(context).getNewsHeadlines(listener, category, null)
    }

    private fun setupProgressDialog() {
        progressDialog = AlertDialog.Builder(context)
            .setMessage(R.string.fetching_news)
            .setCancelable(false)
            .create()
        progressDialog?.show()
    }

    private fun updateProgressDialog(message: String) {
        progressDialog?.setMessage(message)
        progressDialog?.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}