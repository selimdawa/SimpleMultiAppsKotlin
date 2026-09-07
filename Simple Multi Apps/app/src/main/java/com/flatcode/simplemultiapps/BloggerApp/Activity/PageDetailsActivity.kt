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
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.utils.DATA
import com.flatcode.simplemultiapps.databinding.ActivityPageDetailsBinding
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Locale

class PageDetailsActivity : AppCompatActivity() {

    private var _binding: ActivityPageDetailsBinding? = null
    private val binding get() = _binding!!

    private var pageId: String? = null
    private val context: Context = this@PageDetailsActivity

    private val inputDateFormat = SimpleDateFormat(DATA.INPUT_DATE_FORMAT, Locale.ENGLISH)
    private val outputDateFormat = SimpleDateFormat(DATA.OUTPUT_DATE_FORMAT, Locale.ENGLISH)

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        _binding = ActivityPageDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        pageId = intent.getStringExtra(DATA.PAGE_ID)

        with(binding.toolbar) {
            nameSpace.text = getString(R.string.page_details)
            back.visibility = View.VISIBLE
            back.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        }

        loadPageDetails()
    }

    private fun loadPageDetails() {
        val url =
            "${DATA.BLOGGER_BASE_URL}${DATA.BLOG_ID}/${DATA.PAGES}/$pageId?key=${DATA.BLOGGER_API}"

        val stringRequest = StringRequest(
            Request.Method.GET, url, { response ->
            if (response.isNullOrEmpty()) return@StringRequest
            try {
                val jsonObject = JSONObject(response)
                val title = jsonObject.getString(DATA.TITLE)
                val content = jsonObject.getString(DATA.CONTENT)
                val published = jsonObject.getString(DATA.PUBLISHED)
                val displayName =
                    jsonObject.getJSONObject(DATA.AUTHOR).getString(DATA.DISPLAY_NAME)

                val formattedDate = try {
                    val date = inputDateFormat.parse(published)
                    if (date != null) outputDateFormat.format(date) else published
                } catch (_: Exception) {
                    published
                }

                binding.title.text = title
                binding.publishInfo.text =
                    context.getString(R.string.publish_info, displayName, formattedDate)
                binding.webView.loadDataWithBaseURL(null, content, DATA.TEXT_HTML, DATA.UTF_8, null)
            } catch (e: Exception) {
                Toast.makeText(context, e.message ?: DATA.EMPTY, Toast.LENGTH_SHORT).show()
            }
        },
            { error ->
                Toast.makeText(context, error.message ?: DATA.EMPTY, Toast.LENGTH_SHORT).show()
            },
        )

        Volley.newRequestQueue(context).add(stringRequest)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}