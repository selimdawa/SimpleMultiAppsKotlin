package com.flatcode.simplemultiapps.livetv.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.databinding.ActivityLiveTvCategoriesBinding
import com.flatcode.simplemultiapps.livetv.adapter.CategoryAdapter
import com.flatcode.simplemultiapps.livetv.model.Category
import com.flatcode.simplemultiapps.livetv.service.ChannelDataService
import com.flatcode.simplemultiapps.livetv.service.ChannelDataService.OnDataResponse
import com.flatcode.simplemultiapps.utils.DATA
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
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        _binding = ActivityLiveTvCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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
        val url =
            "http://${DATA.IP_LIVE_TV}/mytv/api.php?key=1A4mgi2rBHCJdqggsYVx&id=1&categories=all"

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