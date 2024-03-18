package com.flatcode.simplemultiapps.BloggerApp.Activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.flatcode.simplemultiapps.BloggerApp.Adapter.CommentAdapter
import com.flatcode.simplemultiapps.BloggerApp.Adapter.LabelAdapter
import com.flatcode.simplemultiapps.BloggerApp.Model.Comment
import com.flatcode.simplemultiapps.BloggerApp.Model.Label
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.Unit.DATA
import com.flatcode.simplemultiapps.Unit.THEME
import com.flatcode.simplemultiapps.databinding.ActivityPostDetailsBinding
import org.json.JSONObject
import java.text.MessageFormat
import java.text.SimpleDateFormat
import javax.xml.transform.OutputKeys

class PostDetailsActivity : AppCompatActivity() {

    private var binding: ActivityPostDetailsBinding? = null
    private var postId: String? = null
    private var list: ArrayList<Label>? = null
    private var adapter: LabelAdapter? = null
    private var comments: ArrayList<Comment>? = null
    private var commentAdapter: CommentAdapter? = null
    private val context: Context = this@PostDetailsActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityPostDetailsBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        postId = intent.getStringExtra("postId")

        binding!!.toolbar.nameSpace.setText(R.string.post_details)
        binding!!.toolbar.back.visibility = View.VISIBLE
        binding!!.toolbar.back.setOnClickListener { onBackPressed() }

        loadPostDetails()
    }

    private fun loadPostDetails() {
        val url = ("https://www.googleapis.com/blogger/v3/blogs/" + DATA.BLOG_ID + "/posts/"
                + postId + "?key=" + DATA.BLOGGER_API)
        val stringRequest = StringRequest(Request.Method.GET, url, { response: String? ->
            try {
                val jsonObject = response?.let { JSONObject(it) }
                val id = jsonObject!!.getString("id")
                val title = jsonObject.getString("title")
                val content = jsonObject.getString("content")
                val published = jsonObject.getString("published")
                val url_ = jsonObject.getString("url")
                val displayName = jsonObject.getJSONObject("author").getString("displayName")
                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                val dateFormat2 = SimpleDateFormat("dd/MM/yyyy K:mm a")
                var formattedDate = DATA.EMPTY
                try {
                    val date = dateFormat.parse(published)
                    formattedDate = dateFormat2.format(date)
                } catch (e: Exception) {
                    formattedDate = published
                    e.printStackTrace()
                }
                binding!!.title.text = title
                binding!!.publishInfo.text =
                    MessageFormat.format("By {0}{1}{2}", displayName, DATA.SPACE, formattedDate)
                binding!!.webView.loadDataWithBaseURL(
                    null, content, "text/html", OutputKeys.ENCODING, null
                )
                try {
                    list = ArrayList()
                    list!!.clear()
                    val jsonArray = jsonObject.getJSONArray("labels")
                    for (i in 0 until jsonArray.length()) {
                        val label = jsonArray.getString(i)
                        val label1 = Label(label)
                        list!!.add(label1)
                    }
                    adapter = LabelAdapter(context, list!!)
                    binding!!.recyclerLabels.adapter = adapter
                } catch (e: Exception) {
                }
                loadComments()
            } catch (e: Exception) {
                Toast.makeText(context, DATA.EMPTY + e.message, Toast.LENGTH_SHORT).show()
            }
        }) { error: VolleyError ->
            Toast.makeText(context, DATA.EMPTY + error.message, Toast.LENGTH_SHORT).show()
        }
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }

    private fun loadComments() {
        val url = ("https://www.googleapis.com/blogger/v3/blogs/" + DATA.BLOG_ID + "/posts/"
                + postId + "/comments?key=" + DATA.BLOGGER_API)
        val stringRequest = StringRequest(Request.Method.GET, url,
            { response: String -> onResponse(response) }) { _: VolleyError? -> }
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }

    private fun onResponse(response: String) {
        comments = ArrayList()
        comments!!.clear()
        try {
            val jsonObject = JSONObject(response)
            val jsonArray = jsonObject.getJSONArray("items")
            for (i in 0 until jsonArray.length()) {
                val jsonObject1 = jsonArray.getJSONObject(i)
                val id = jsonObject1.getString("id")
                val published = jsonObject1.getString("published")
                val content = jsonObject1.getString("content")
                val displayName = jsonObject1.getJSONObject("author").getString("displayName")
                val profileImage =
                    "http:" + jsonObject1.getJSONObject("author").getJSONObject("image")
                        .getString("url")
                val comment = Comment(
                    DATA.EMPTY + id, DATA.EMPTY + displayName,
                    DATA.EMPTY + profileImage, DATA.EMPTY + published,
                    DATA.EMPTY + content
                )
                comments!!.add(comment)
            }
            commentAdapter = CommentAdapter(context, comments!!)
            binding!!.recyclerComments.adapter = commentAdapter
        } catch (e: Exception) {
        }
    }
}