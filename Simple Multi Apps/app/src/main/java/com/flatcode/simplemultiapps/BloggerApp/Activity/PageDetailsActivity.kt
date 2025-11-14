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
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.Unit.DATA
import com.flatcode.simplemultiapps.Unit.THEME
import com.flatcode.simplemultiapps.databinding.ActivityPageDetailsBinding
import org.json.JSONObject
import java.text.MessageFormat
import java.text.SimpleDateFormat
import javax.xml.transform.OutputKeys

class PageDetailsActivity : AppCompatActivity() {

    private var binding: ActivityPageDetailsBinding? = null
    var pageId: String? = null
    var context: Context = this@PageDetailsActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityPageDetailsBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        pageId = intent.getStringExtra("pageId")

        binding!!.toolbar.nameSpace.text = getString(R.string.page_details)
        binding!!.toolbar.back.visibility = View.VISIBLE
        binding!!.toolbar.back.setOnClickListener { onBackPressed() }

        loadPageDetails()
    }

    private fun loadPageDetails() {
        val url = ("https://www.googleapis.com/blogger/v3/blogs/" + DATA.BLOG_ID + "/pages/"
                + pageId + "?key=" + DATA.BLOGGER_API)
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
            } catch (e: Exception) {
                Toast.makeText(context, DATA.EMPTY + e.message, Toast.LENGTH_SHORT).show()
            }
        }) { error: VolleyError ->
            Toast.makeText(context, DATA.EMPTY + error.message, Toast.LENGTH_SHORT).show()
        }
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }
}