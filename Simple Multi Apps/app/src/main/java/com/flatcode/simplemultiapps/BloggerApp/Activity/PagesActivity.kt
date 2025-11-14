package com.flatcode.simplemultiapps.BloggerApp.Activity

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.flatcode.simplemultiapps.BloggerApp.Adapter.PagesAdapter
import com.flatcode.simplemultiapps.BloggerApp.Model.Page
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.Unit.DATA
import com.flatcode.simplemultiapps.Unit.THEME
import com.flatcode.simplemultiapps.databinding.ActivityBloggerPagesBinding
import org.json.JSONObject

class PagesActivity : AppCompatActivity() {

    private var binding: ActivityBloggerPagesBinding? = null
    private var pages: ArrayList<Page>? = null
    private var adapter: PagesAdapter? = null
    var context: Context = this@PagesActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityBloggerPagesBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        binding!!.toolbar.nameSpace.setText(R.string.blogger_pages)
        binding!!.toolbar.back.visibility = View.VISIBLE
        binding!!.toolbar.back.setOnClickListener { onBackPressed() }

        loadPages()
    }

    private fun loadPages() {
        val dialog = ProgressDialog(context)
        dialog.setTitle("Please wait...")
        dialog.setMessage("Loading Pages")
        dialog.show()
        val url =
            "https://www.googleapis.com/blogger/v3/blogs/" + DATA.BLOG_ID + "/pages?key=" + DATA.BLOGGER_API
        val stringRequest = StringRequest(Request.Method.GET, url, { response: String? ->
            dialog.dismiss()
            try {
                val jsonObject = response?.let { JSONObject(it) }
                val jsonArray = jsonObject!!.getJSONArray("items")
                pages = ArrayList()
                pages!!.clear()
                for (i in 0 until jsonArray.length()) {
                    try {
                        val jsonObject1 = jsonArray.getJSONObject(i)
                        val id = jsonObject1.getString("id")
                        val title = jsonObject1.getString("title")
                        val content = jsonObject1.getString("content")
                        val published = jsonObject1.getString("published")
                        val updated = jsonObject1.getString("updated")
                        val url_ = jsonObject1.getString("url")
                        val selfLink = jsonObject1.getString("selfLink")
                        val displayName =
                            jsonObject1.getJSONObject("author").getString("displayName")
                        val image = jsonObject1.getJSONObject("author").getJSONObject("image")
                            .getString("url")
                        val page = Page(
                            DATA.EMPTY + displayName, DATA.EMPTY + content,
                            DATA.EMPTY + id, DATA.EMPTY + published, DATA.EMPTY
                                    + selfLink, DATA.EMPTY + title,
                            DATA.EMPTY + updated, DATA.EMPTY + url_
                        )
                        pages!!.add(page)
                    } catch (e: Exception) {
                        Toast.makeText(context, DATA.EMPTY + e.message, Toast.LENGTH_SHORT).show()
                    }
                }
                adapter = PagesAdapter(context, pages!!)
                binding!!.recyclerView.adapter = adapter
            } catch (e: Exception) {
            }
        }) { error: VolleyError ->
            dialog.dismiss()
            Toast.makeText(context, DATA.EMPTY + error.message, Toast.LENGTH_SHORT).show()
        }
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }
}