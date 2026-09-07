@file:Suppress("SpellCheckingInspection")
package com.flatcode.simplemultiapps.livetv.activity

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        _binding = ActivityLiveTvCategoryDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dataService = ChannelDataService(this)
        categoryName = intent.getStringExtra(DATA.CATEGORY_NAME)

        val extractedName = if (categoryName.isNullOrEmpty()) {
            @Suppress("DEPRECATION")
            val serializable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getSerializableExtra(DATA.CATEGORY, Category::class.java)
            } else {
                intent.getSerializableExtra(DATA.CATEGORY) as? Category
            }
            category = serializable
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
            "${DATA.HTTP}://${DATA.IP_LIVE_TV}/mytv/api.php?key=${DATA.LIVETV_API_KEY}&id=1&cat=$extractedName"

        adapter = ChannelAdapter(DATA.DETAILS)
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
                            id = channelData.getInt(DATA.ID),
                            name = channelData.getString(DATA.NAME),
                            description = channelData.getString(DATA.DESCRIPTION),
                            thumbnail = channelData.getString(DATA.THUMBNAIL),
                            liveUrl = channelData.getString(DATA.LIVE_URL),
                            facebook = channelData.getString(DATA.FACEBOOK_KEY),
                            twitter = channelData.getString(DATA.TWITTER_KEY),
                            youtube = channelData.getString(DATA.YOUTUBE),
                            website = channelData.getString(DATA.WEBSITE_KEY),
                            category = channelData.getString(DATA.CATEGORY),
                        )
                        channels.add(c)
                    } catch (_: JSONException) {
                    }
                }
                adapter.submitList(channels)
            }

            override fun onError(error: String?) {}
        },)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}