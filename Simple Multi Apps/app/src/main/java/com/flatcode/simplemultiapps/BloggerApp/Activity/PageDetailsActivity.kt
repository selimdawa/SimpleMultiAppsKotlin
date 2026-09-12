package com.flatcode.simplemultiapps.bloggerapp.activity

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.R.attr.colorError
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.bloggerapp.viewmodel.PageDetailsViewModel
import com.flatcode.simplemultiapps.databinding.ActivityPageDetailsBinding
import java.text.SimpleDateFormat
import java.util.Locale

class PageDetailsActivity : AppCompatActivity() {

    private var _binding: ActivityPageDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PageDetailsViewModel by viewModels()
    private val context: Context = this@PageDetailsActivity

    private val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
    private val outputDateFormat = SimpleDateFormat("dd/MM/yyyy K:mm a", Locale.ENGLISH)

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        _binding = ActivityPageDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val pageId = intent.getStringExtra("pageId") ?: ""

        with(binding.toolbar) {
            nameSpace.text = getString(R.string.page_details)
            back.visibility = View.VISIBLE
            back.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        }

        observeViewModel()

        if (viewModel.page.value == null) {
            viewModel.loadPageDetails(pageId)
        }
    }

    private fun observeViewModel() {
        viewModel.page.observe(this) { page ->
            val formattedDate = try {
                val date = inputDateFormat.parse(page.published ?: "")
                if (date != null) outputDateFormat.format(date) else page.published ?: ""
            } catch (_: Exception) {
                page.published ?: ""
            }

            binding.title.text = page.title
            binding.publishInfo.text =
                context.getString(R.string.publish_info, page.authorName, formattedDate)

            val typedValue = TypedValue()
            theme.resolveAttribute(colorError, typedValue, true)
            val hexColor = String.format("#%06X", 0xFFFFFF and typedValue.data)

            val styledContent =
                "<html><head><style>body { color: $hexColor; font-family: sans-serif; line-height: 1.6; } a { color: #2196F3; }</style></head><body>${page.content}</body></html>"

            binding.webView.setBackgroundColor(0)
            binding.webView.loadDataWithBaseURL(null, styledContent, "text/html", "UTF-8", null)
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