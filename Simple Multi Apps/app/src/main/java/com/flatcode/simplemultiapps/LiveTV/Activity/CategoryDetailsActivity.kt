package com.flatcode.simplemultiapps.livetv.activity

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.flatcode.simplemultiapps.databinding.ActivityLiveTvCategoryDetailsBinding
import com.flatcode.simplemultiapps.livetv.adapter.ChannelAdapter
import com.flatcode.simplemultiapps.livetv.model.Category
import com.flatcode.simplemultiapps.livetv.model.Channel
import com.flatcode.simplemultiapps.livetv.service.ChannelDataService
import com.flatcode.simplemultiapps.livetv.service.ChannelDataService.OnDataResponse
import com.flatcode.simplemultiapps.utils.DATA
import org.json.JSONException
import org.json.JSONObject

class CategoryDetailsActivity : AppCompatActivity() {

    private var _binding: ActivityLiveTvCategoryDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ChannelAdapter
    private val channels = ArrayList<Channel>()
    private var dataService: ChannelDataService? = null
    val context: Context = this@CategoryDetailsActivity
    private var categoryName: String? = null
    private var category: Category? = null
    private var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLiveTvCategoryDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataService = ChannelDataService(this)
        categoryName = intent.getStringExtra("categoryName")

        val extractedName = if (categoryName.isNullOrEmpty()) {
            @Suppress("DEPRECATION") category =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getSerializableExtra("category", Category::class.java)
                } else {
                    intent.getSerializableExtra("category") as? Category
                }
            category?.name
        } else {
            categoryName
        }

        with(binding.toolbar) {
            nameSpace.text = extractedName.orEmpty()
            back.visibility = View.VISIBLE
            back.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        }

        url =
            "http://${DATA.IP_LIVE_TV}/mytv/api.php?key=1A4mgi2rBHCJdqggsYVx&id=1&cat=$extractedName"

        adapter = ChannelAdapter("details")
        binding.recyclerView.adapter = adapter

        loadChannels()
    }

    private fun loadChannels() {
        dataService?.getChannelData(url, object : OnDataResponse {
            override fun onResponse(response: JSONObject) {
                channels.clear()
                for (i in 0 until response.length()) {
                    try {
                        val channelData = response.getJSONObject(i.toString())
                        val c = Channel(
                            id = channelData.getInt("id"),
                            name = channelData.getString("name"),
                            description = channelData.getString("description"),
                            thumbnail = channelData.getString("thumbnail"),
                            liveUrl = channelData.getString("live_url"),
                            facebook = channelData.getString("facebook"),
                            twitter = channelData.getString("twitter"),
                            youtube = channelData.getString("youtube"),
                            website = channelData.getString("website"),
                            category = channelData.getString("category")
                        )
                        channels.add(c)
                    } catch (_: JSONException) {
                    }
                }
                adapter.submitList(channels)
            }

            override fun onError(error: String?) {}
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}