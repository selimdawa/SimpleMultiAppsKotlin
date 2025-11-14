package com.flatcode.simplemultiapps.LiveTV.Activity

import android.content.Context
import android.os.Bundle
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

    private var binding: ActivityLiveTvCategoriesBinding? = null
    var categoryAdapter: CategoryAdapter? = null
    var categoryList: MutableList<Category>? = null
    var dataService: ChannelDataService? = null
    var context: Context = this@CategoriesActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityLiveTvCategoriesBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        binding!!.toolbar.nameSpace.setText(R.string.categories)
        dataService = ChannelDataService(this)
        categoryList = ArrayList()
        categoryAdapter = CategoryAdapter(context, categoryList!!)
        binding!!.recyclerView.adapter = categoryAdapter

        binding!!.toolbar.back.setOnClickListener { onBackPressed() }
        dataService!!.getChannelData("http://" + DATA.IP_LIVE_TV + "/mytv/api.php?key=1A4mgi2rBHCJdqggsYVx&id=1&categories=all",
            object : OnDataResponse {
                override fun onResponse(response: JSONObject) {
                    for (i in 0 until response.length()) {
                        try {
                            val categoryData = response.getJSONObject(i.toString())
                            val category = Category(
                                categoryData.getInt("id"),
                                categoryData.getString("name"),
                                categoryData.getString("image_url")
                            )
                            categoryList!!.add(category)
                            categoryAdapter!!.notifyDataSetChanged()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onError(error: String?) {}
            })
    }
}