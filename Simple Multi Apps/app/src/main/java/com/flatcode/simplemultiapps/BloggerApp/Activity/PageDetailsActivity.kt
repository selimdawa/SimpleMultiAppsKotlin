package com.flatcode.simplemultiapps.BloggerApp.Activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.Unit.DATA
import com.flatcode.simplemultiapps.Unit.THEME
import com.flatcode.simplemultiapps.databinding.ActivityPageDetailsBinding
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Locale

class PageDetailsActivity : AppCompatActivity() {

    private var _binding: ActivityPageDetailsBinding? = null
    private val binding get() = _binding!!

    private var pageId: String? = null
    private val context: Context = this@PageDetailsActivity

    private val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
    private val outputDateFormat = SimpleDateFormat("dd/MM/yyyy K:mm a", Locale.ENGLISH)

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        _binding = ActivityPageDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pageId = intent.getStringExtra("pageId")

        with(binding.toolbar) {
            nameSpace.text = getString(R.string.page_details)
            back.visibility = View.VISIBLE
            back.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        }

        loadPageDetails()
    }

    private fun loadPageDetails() {
        val url = "https://googleapis.com{DATA.BLOG_ID}/pages/$pageId?key=${DATA.BLOGGER_API}"

        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            try {
                val jsonObject = JSONObject(response ?: DATA.EMPTY)
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
            } catch (e: Exception) {
                Toast.makeText(context, e.message ?: DATA.EMPTY, Toast.LENGTH_SHORT).show()
            }
        }) { error ->
            Toast.makeText(context, error.message ?: DATA.EMPTY, Toast.LENGTH_SHORT).show()
        }

        Volley.newRequestQueue(context).add(stringRequest)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}