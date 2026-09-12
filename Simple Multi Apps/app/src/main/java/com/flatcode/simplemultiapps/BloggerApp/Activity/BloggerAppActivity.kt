package com.flatcode.simplemultiapps.bloggerapp.activity

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.bloggerapp.adapter.PostAdapter
import com.flatcode.simplemultiapps.bloggerapp.viewmodel.MainViewModel
import com.flatcode.simplemultiapps.databinding.ActivityBloggerAppBinding
import com.flatcode.simplemultiapps.utils.DATA
import com.flatcode.simplemultiapps.utils.openActivity

class BloggerAppActivity : AppCompatActivity() {

    private var _binding: ActivityBloggerAppBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()
    private var adapter: PostAdapter? = null
    private val context: Context = this@BloggerAppActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        _binding = ActivityBloggerAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupUI()
        observeViewModel()

        if (viewModel.posts.value == null) {
            viewModel.loadPosts(true)
        }
    }

    private fun setupUI() {
        with(binding.toolbar) {
            nameSpace.text = getString(R.string.blogger_name)
            close.setOnClickListener { resetSearch() }
            pages.setOnClickListener { context.openActivity(PagesActivity::class.java) }
            search.setOnClickListener {
                toolbar.visibility = View.GONE
                toolbarSearch.visibility = View.VISIBLE
                DATA.searchStatus = true
            }
            postSearch.visibility = View.GONE

            textSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val query = s.toString().trim()
                    adapter?.filter(query)
                    val isEmpty = adapter?.itemCount == 0
                    binding.noResultsText.visibility =
                        if (isEmpty && query.isNotEmpty()) View.VISIBLE else View.GONE
                    binding.loadMoreLayout.visibility =
                        if (query.isEmpty() && viewModel.getNextToken() != DATA.END) View.VISIBLE else View.GONE
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (DATA.searchStatus) {
                    resetSearch()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })

        binding.loadMore.setOnClickListener {
            val query = binding.toolbar.textSearch.text.toString().trim()
            if (query.isEmpty()) {
                viewModel.loadPosts()
            } else {
                viewModel.searchPosts(query)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.posts.observe(this) { posts ->
            if (adapter == null) {
                adapter = PostAdapter(context, ArrayList(posts))
                binding.recyclerView.adapter = adapter
            } else {
                adapter?.updateList(ArrayList(posts))
            }

            val queryText = binding.toolbar.textSearch.text.toString().trim()
            if (queryText.isNotEmpty()) {
                adapter?.filter(queryText)
            }
        }

        viewModel.loading.observe(this) { isLoading ->
            setLoading(
                isLoading, viewModel.getNextToken() == "1" || viewModel.getNextToken() == DATA.EMPTY
            )
        }

        viewModel.error.observe(this) { errorMsg ->
            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
        }
    }

    private fun resetSearch() {
        binding.toolbar.toolbar.visibility = View.VISIBLE
        binding.toolbar.toolbarSearch.visibility = View.GONE
        binding.toolbar.textSearch.setText("")
        DATA.searchStatus = false
        adapter?.filter("")
        binding.noResultsText.visibility = View.GONE
        binding.loadMoreLayout.visibility =
            if (viewModel.getNextToken() == DATA.END) View.GONE else View.VISIBLE
    }

    private fun setLoading(isLoading: Boolean, isInitial: Boolean) {
        val binding = _binding ?: return
        if (isLoading) {
            if (isInitial) {
                binding.progressBar.visibility = View.VISIBLE
                binding.loadMoreLayout.visibility = View.GONE
            } else {
                binding.loadMore.visibility = View.GONE
                binding.loadMoreProgress.visibility = View.VISIBLE
            }
        } else {
            binding.progressBar.visibility = View.GONE
            binding.loadMoreProgress.visibility = View.GONE
            if (viewModel.getNextToken() == DATA.END) {
                binding.loadMoreLayout.visibility = View.GONE
            } else {
                binding.loadMoreLayout.visibility = View.VISIBLE
                binding.loadMore.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}