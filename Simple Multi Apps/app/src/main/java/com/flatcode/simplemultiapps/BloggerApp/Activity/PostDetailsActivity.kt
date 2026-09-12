package com.flatcode.simplemultiapps.bloggerapp.activity

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.R.attr.colorError
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.bloggerapp.adapter.CommentAdapter
import com.flatcode.simplemultiapps.bloggerapp.adapter.LabelAdapter
import com.flatcode.simplemultiapps.bloggerapp.model.Comment
import com.flatcode.simplemultiapps.bloggerapp.model.Label
import com.flatcode.simplemultiapps.databinding.ActivityPostDetailsBinding
import com.flatcode.simplemultiapps.utils.DATA
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import java.text.SimpleDateFormat
import java.util.Locale

class PostDetailsActivity : AppCompatActivity() {

    private var _binding: ActivityPostDetailsBinding? = null
    private val binding get() = _binding!!

    private var postId: String? = null
    private val list = ArrayList<Label>()
    private var adapter: LabelAdapter? = null
    private val comments = ArrayList<Comment>()
    private var commentAdapter: CommentAdapter? = null
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

        postId = intent.getStringExtra("postId")

        with(binding.toolbar) {
            nameSpace.setText(R.string.post_details)
            back.visibility = View.VISIBLE
            back.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        }

        loadPostDetails()
    }

    private fun loadPostDetails() {
        val url = "https://www.blogger.com/feeds/${DATA.BLOG_ID}/posts/default/$postId"

        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            try {
                val doc = Jsoup.parse(response ?: DATA.EMPTY, "", Parser.xmlParser())
                val entry = doc.selectFirst("entry")

                if (entry != null) {
                    val title = entry.selectFirst("title")?.text() ?: ""
                    val content = entry.selectFirst("content")?.text() ?: ""
                    val published = entry.selectFirst("published")?.text() ?: ""
                    val displayName = entry.select("author name").first()?.text() ?: DATA.UNKNOWN

                    val formattedDate = try {
                        val date = inputDateFormat.parse(published)
                        if (date != null) outputDateFormat.format(date) else published
                    } catch (_: Exception) {
                        published
                    }

                    binding.title.text = title
                    binding.publishInfo.text =
                        context.getString(R.string.publish_info, displayName, formattedDate)

                    val typedValue = TypedValue()
                    theme.resolveAttribute(colorError, typedValue, true)
                    val hexColor = String.format("#%06X", 0xFFFFFF and typedValue.data)

                    val styledContent =
                        "<html><head><style>body { color: $hexColor; font-family: sans-serif; line-height: 1.6; padding: 10px; } a { color: #2196F3; } img { max-width: 100%; height: auto; }</style></head><body>$content</body></html>"

                    binding.webView.setBackgroundColor(0)
                    binding.webView.loadDataWithBaseURL(
                        null, styledContent, "text/html", "UTF-8", null
                    )

                    try {
                        list.clear()
                        val categories = entry.select("category")
                        for (category in categories) {
                            val term = category.attr("term")
                            if (term.isNotEmpty()) {
                                list.add(Label(term))
                            }
                        }
                        adapter = LabelAdapter(context, list)
                        binding.recyclerLabels.adapter = adapter
                    } catch (_: Exception) {
                    }

                    loadComments()
                }
            } catch (e: Exception) {
                Toast.makeText(context, e.message ?: DATA.EMPTY, Toast.LENGTH_SHORT).show()
            }
        }) { error ->
            Toast.makeText(context, error.message ?: DATA.EMPTY, Toast.LENGTH_SHORT).show()
        }

        Volley.newRequestQueue(context).add(stringRequest)
    }

    private fun loadComments() {
        val url = "https://www.blogger.com/feeds/${DATA.BLOG_ID}/$postId/comments/default"

        val stringRequest = StringRequest(
            Request.Method.GET, url, { response -> onResponse(response) }) { _: VolleyError? -> }

        Volley.newRequestQueue(context).add(stringRequest)
    }

    private fun onResponse(response: String) {
        comments.clear()
        try {
            val doc = Jsoup.parse(response, "", Parser.xmlParser())
            val entries = doc.select("entry")

            for (entry in entries) {
                val id = entry.selectFirst("id")?.text()?.split("-")?.last() ?: ""
                val published = entry.selectFirst("published")?.text() ?: ""
                val content = entry.selectFirst("content")?.text() ?: ""
                val displayName = entry.select("author name").first()?.text() ?: DATA.UNKNOWN
                val profileImage = entry.select("author gd|image").attr("src").ifEmpty {
                    entry.select("author link[rel=image]").attr("href").ifEmpty {
                        "https://www.blogger.com/img/blogger-logotype-color-black-caps.png"
                    }
                }

                val comment = Comment(id, displayName, profileImage, published, content)
                comments.add(comment)
            }
            commentAdapter = CommentAdapter(context, comments)
            binding.recyclerComments.adapter = commentAdapter
        } catch (_: Exception) {
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}