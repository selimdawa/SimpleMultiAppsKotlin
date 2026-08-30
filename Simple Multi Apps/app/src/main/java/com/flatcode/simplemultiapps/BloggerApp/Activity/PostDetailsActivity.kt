package com.flatcode.simplemultiapps.bloggerapp.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.flatcode.simplemultiapps.bloggerapp.adapter.CommentAdapter
import com.flatcode.simplemultiapps.bloggerapp.adapter.LabelAdapter
import com.flatcode.simplemultiapps.bloggerapp.model.Comment
import com.flatcode.simplemultiapps.bloggerapp.model.Label
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.utils.DATA
import com.flatcode.simplemultiapps.databinding.ActivityPostDetailsBinding
import org.json.JSONObject
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

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
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
        val url = "https://www.googleapis.com/blogger/v3/blogs/${DATA.BLOG_ID}/posts/$postId?key=${DATA.BLOGGER_API}"

        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            if (response.isNullOrEmpty()) return@StringRequest
            try {
                val jsonObject = JSONObject(response)
                val title = jsonObject.getString("title")
                val content = jsonObject.getString("content")
                val published = jsonObject.getString("published")
                val displayName = jsonObject.getJSONObject("author").getString("displayName")

                val formattedDate = try {
                    val date = inputDateFormat.parse(published)
                    if (date != null) outputDateFormat.format(date) else published
                } catch (_: Exception) {
                    published
                }

                binding.title.text = title
                binding.publishInfo.text = context.getString(R.string.publish_info, displayName, formattedDate)
                binding.webView.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null)

                try {
                    list.clear()
                    val jsonArray = jsonObject.getJSONArray("labels")
                    for (i in 0 until jsonArray.length()) {
                        list.add(Label(jsonArray.getString(i)))
                    }
                    adapter = LabelAdapter(context, list)
                    binding.recyclerLabels.adapter = adapter
                } catch (_: Exception) {
                }

                loadComments()
            } catch (e: Exception) {
                Toast.makeText(context, e.message ?: DATA.EMPTY, Toast.LENGTH_SHORT).show()
            }
        }) { error ->
            Toast.makeText(context, error.message ?: DATA.EMPTY, Toast.LENGTH_SHORT).show()
        }

        Volley.newRequestQueue(context).add(stringRequest)
    }

    private fun loadComments() {
        val url = "https://www.googleapis.com/blogger/v3/blogs/${DATA.BLOG_ID}/posts/$postId/comments?key=${DATA.BLOGGER_API}"

        val stringRequest = StringRequest(Request.Method.GET, url,
            { response -> onResponse(response) }) { _: VolleyError? -> }

        Volley.newRequestQueue(context).add(stringRequest)
    }

    private fun onResponse(response: String) {
        comments.clear()
        try {
            val jsonObject = JSONObject(response)
            val jsonArray = jsonObject.getJSONArray("items")
            for (i in 0 until jsonArray.length()) {
                val jsonObject1 = jsonArray.getJSONObject(i)
                val id = jsonObject1.getString("id")
                val published = jsonObject1.getString("published")
                val content = jsonObject1.getString("content")
                val displayName = jsonObject1.getJSONObject("author").getString("displayName")
                val profileImage = "http:${jsonObject1.getJSONObject("author").getJSONObject("image").getString("url")}"

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