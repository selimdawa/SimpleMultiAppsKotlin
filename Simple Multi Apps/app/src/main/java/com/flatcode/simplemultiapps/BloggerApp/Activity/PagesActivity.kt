package com.flatcode.simplemultiapps.bloggerapp.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.bloggerapp.adapter.PagesAdapter
import com.flatcode.simplemultiapps.bloggerapp.viewmodel.PagesViewModel
import com.flatcode.simplemultiapps.databinding.ActivityBloggerPagesBinding

class PagesActivity : AppCompatActivity() {

    private var _binding: ActivityBloggerPagesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PagesViewModel by viewModels()
    private var adapter: PagesAdapter? = null
    val context: Context = this@PagesActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        _binding = ActivityBloggerPagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        with(binding.toolbar) {
            nameSpace.setText(R.string.blogger_pages)
            back.visibility = View.VISIBLE
            back.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        }

        observeViewModel()
        if (viewModel.pages.value == null) {
            viewModel.loadPages()
        }
    }

    private fun observeViewModel() {
        viewModel.pages.observe(this) { pages ->
            adapter = PagesAdapter(context, ArrayList(pages))
            binding.recyclerView.adapter = adapter
        }

        viewModel.loading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { errorMsg ->
            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}