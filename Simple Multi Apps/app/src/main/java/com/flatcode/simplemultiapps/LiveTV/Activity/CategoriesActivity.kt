package com.flatcode.simplemultiapps.LiveTV.Activity

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.flatcode.simplemultiapps.LiveTV.Adapter.CategoryAdapter
import com.flatcode.simplemultiapps.LiveTV.Model.Category
import com.flatcode.simplemultiapps.LiveTV.Service.ChannelDataService
import com.flatcode.simplemultiapps.LiveTV.Service.ChannelDataService.OnDataResponse
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.Unit.DATA
import com.flatcode.simplemultiapps.Unit.THEME
import com.flatcode.simplemultiapps.databinding.ActivityLiveTvCategoriesBinding
import org.json.JSONException
import org.json.JSONObject

class CategoriesActivity : AppCompatActivity() {

    private var _binding: ActivityLiveTvCategoriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryAdapter: CategoryAdapter
    private val categoryList = ArrayList<Category>()
    private var dataService: ChannelDataService? = null
    val context: Context = this@CategoriesActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        _binding = ActivityLiveTvCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding.toolbar) {
            nameSpace.setText(R.string.categories)
            back.visibility = View.VISIBLE
            back.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        }

        dataService = ChannelDataService(this)
        categoryAdapter = CategoryAdapter()
        binding.recyclerView.adapter = categoryAdapter

        loadCategories()
    }

    private fun loadCategories() {
        val url = "http://${DATA.IP_LIVE_TV}/mytv/api.php?key=1A4mgi2rBHCJdqggsYVx&id=1&categories=all"

        dataService?.getChannelData(url, object : OnDataResponse {
            override fun onResponse(response: JSONObject) {
                categoryList.clear()
                for (i in 0 until response.length()) {
                    try {
                        val categoryData = response.getJSONObject(i.toString())
                        val category = Category(
                            id = categoryData.getInt("id"),
                            name = categoryData.getString("name"),
                            imageUrl = categoryData.getString("image_url")
                        )
                        categoryList.add(category)
                    } catch (_: JSONException) {
                    }
                }
                categoryAdapter.submitList(categoryList)
            }

            override fun onError(error: String?) {}
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}