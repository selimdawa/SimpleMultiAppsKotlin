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
import com.flatcode.simplemultiapps.bloggerapp.adapter.CommentAdapter
import com.flatcode.simplemultiapps.bloggerapp.adapter.LabelAdapter
import com.flatcode.simplemultiapps.bloggerapp.model.Label
import com.flatcode.simplemultiapps.bloggerapp.viewmodel.PostDetailsViewModel
import com.flatcode.simplemultiapps.databinding.ActivityPostDetailsBinding
import java.text.SimpleDateFormat
import java.util.Locale

class PostDetailsActivity : AppCompatActivity() {

    private var _binding: ActivityPostDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PostDetailsViewModel by viewModels()
    private val context: Context = this@PostDetailsActivity

    private val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
    private val outputDateFormat = SimpleDateFormat("dd/MM/yyyy K:mm a", Locale.ENGLISH)

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        _binding = ActivityPostDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val postId = intent.getStringExtra("postId") ?: ""

        with(binding.toolbar) {
            nameSpace.setText(R.string.post_details)
            back.visibility = View.VISIBLE
            back.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        }

        observeViewModel()

        if (viewModel.post.value == null) {
            viewModel.loadPostDetails(postId)
        }
    }

    private fun observeViewModel() {
        viewModel.post.observe(this) { post ->
            val formattedDate = try {
                val date = inputDateFormat.parse(post.published ?: "")
                if (date != null) outputDateFormat.format(date) else post.published ?: ""
            } catch (_: Exception) {
                post.published ?: ""
            }

            binding.title.text = post.title
            binding.publishInfo.text =
                context.getString(R.string.publish_info, post.authorName, formattedDate)

            val typedValue = TypedValue()
            theme.resolveAttribute(colorError, typedValue, true)
            val hexColor = String.format("#%06X", 0xFFFFFF and typedValue.data)

            val styledContent =
                "<html><head><style>body { color: $hexColor; font-family: sans-serif; line-height: 1.6; padding: 10px; } a { color: #2196F3; } img { max-width: 100%; height: auto; }</style></head><body>${post.content}</body></html>"

            binding.webView.setBackgroundColor(0)
            binding.webView.loadDataWithBaseURL(null, styledContent, "text/html", "UTF-8", null)

            val labelList = post.labels?.map { Label(it) } ?: emptyList()
            binding.recyclerLabels.adapter = LabelAdapter(context, ArrayList(labelList))
        }

        viewModel.comments.observe(this) { comments ->
            binding.recyclerComments.adapter = CommentAdapter(context, ArrayList(comments))
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