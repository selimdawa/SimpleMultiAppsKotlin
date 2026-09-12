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
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.databinding.ActivityPageDetailsBinding
import com.flatcode.simplemultiapps.utils.DATA
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
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
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        _binding = ActivityPageDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        pageId = intent.getStringExtra("pageId")

        with(binding.toolbar) {
            nameSpace.text = getString(R.string.page_details)
            back.visibility = View.VISIBLE
            back.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        }

        loadPageDetails()
    }

    private fun loadPageDetails() {
        val url = "https://www.blogger.com/feeds/${DATA.BLOG_ID}/pages/default/$pageId"

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
                        "<html><head><style>body { color: $hexColor; font-family: sans-serif; line-height: 1.6; } a { color: #2196F3; }</style></head><body>$content</body></html>"

                    binding.webView.setBackgroundColor(0)
                    binding.webView.loadDataWithBaseURL(
                        null, styledContent, "text/html", "UTF-8", null
                    )
                }
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