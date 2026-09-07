@file:Suppress("SpellCheckingInspection")
package com.flatcode.simplemultiapps.livetv.activity

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.flatcode.simplemultiapps.livetv.adapter.ChannelAdapter
import com.flatcode.simplemultiapps.livetv.model.Channel
import com.flatcode.simplemultiapps.livetv.service.ChannelDataService
import com.flatcode.simplemultiapps.livetv.service.ChannelDataService.OnDataResponse
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.utils.DATA
import com.flatcode.simplemultiapps.utils.intent1
import com.flatcode.simplemultiapps.databinding.ActivityLiveTvBinding
import org.json.JSONException
import org.json.JSONObject

class LiveTVActivity : AppCompatActivity() {

    private var _binding: ActivityLiveTvBinding? = null
    private val binding get() = _binding!!

    private lateinit var bigSliderAdapter: ChannelAdapter
    private lateinit var newsChannelAdapter: ChannelAdapter
    private lateinit var sportsChannelAdapter: ChannelAdapter
    private lateinit var enterChannelAdapter: ChannelAdapter

    private val channelList = ArrayList<Channel>()
    private val newsChannels = ArrayList<Channel>()
    private val sportsChannel = ArrayList<Channel>()
    private val enterChannel = ArrayList<Channel>()

    private var service: ChannelDataService? = null
    val context: Context = this@LiveTVActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        _binding = ActivityLiveTvBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.toolbar.nameSpace.setText(R.string.live_tv)
        service = ChannelDataService(this)

        setupRecyclerViews()
        setupClickListeners()
        loadAllChannels()
    }

    private fun setupRecyclerViews() {
        bigSliderAdapter = ChannelAdapter(DATA.SLIDER)
        binding.bigSliderList.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = bigSliderAdapter
        }

        newsChannelAdapter = ChannelAdapter(DATA.DETAILS)
        binding.newsChannelList.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = newsChannelAdapter
        }

        sportsChannelAdapter = ChannelAdapter(DATA.DETAILS)
        binding.sportsChannelList.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = sportsChannelAdapter
        }

        enterChannelAdapter = ChannelAdapter(DATA.DETAILS)
        binding.enterChannelList.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = enterChannelAdapter
        }
    }

    private fun setupClickListeners() {
        binding.toolbar.categories.setOnClickListener {
            context.intent1(CategoriesActivity::class.java)
        }
        binding.more.setOnClickListener {
            startCategoryDetailActivity(DATA.NEWS)
        }
        binding.more2.setOnClickListener {
            startCategoryDetailActivity(DATA.SPORTS)
        }
        binding.more3.setOnClickListener {
            startCategoryDetailActivity(DATA.ENTERTAINMENT)
        }
    }

    private fun startCategoryDetailActivity(categoryName: String) {
        context.intent1(CategoryDetailsActivity::class.java) {
            putExtra(DATA.CATEGORY_NAME, categoryName)
        }
    }

    private fun loadAllChannels() {
        val baseUrl =
            "${DATA.HTTP}://${DATA.IP_LIVE_TV}/mytv/api.php?key=${DATA.LIVETV_API_KEY}&id=1"
        getSliderData("$baseUrl&channels=all")
        getNewsChannels("$baseUrl&cat=${DATA.NEWS}")
        getSportsChannel("$baseUrl&cat=${DATA.SPORTS}")
        getEnterChannel("$baseUrl&cat=${DATA.ENTERTAINMENT}")
    }

    private fun parseChannel(channelData: JSONObject): Channel {
        return Channel(
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
    }

    private fun getSliderData(url: String) {
        service?.getChannelData(
            url,
            object : OnDataResponse {
                override fun onResponse(response: JSONObject) {
                channelList.clear()
                for (i in 0 until response.length()) {
                    try {
                        val channelData = response.getJSONObject(i.toString())
                        channelList.add(parseChannel(channelData))
                    } catch (_: JSONException) {
                    }
                }
                bigSliderAdapter.submitList(channelList)
            }

            override fun onError(error: String?) {}
        })
    }

    private fun getNewsChannels(url: String) {
        service?.getChannelData(url, object : OnDataResponse {
            override fun onResponse(response: JSONObject) {
                newsChannels.clear()
                for (i in 0 until response.length()) {
                    try {
                        val channelData = response.getJSONObject(i.toString())
                        newsChannels.add(parseChannel(channelData))
                    } catch (_: JSONException) {
                    }
                }
                newsChannelAdapter.submitList(newsChannels)
            }

            override fun onError(error: String?) {}
        })
    }

    private fun getSportsChannel(url: String) {
        service?.getChannelData(url, object : OnDataResponse {
            override fun onResponse(response: JSONObject) {
                sportsChannel.clear()
                for (i in 0 until response.length()) {
                    try {
                        val channelData = response.getJSONObject(i.toString())
                        sportsChannel.add(parseChannel(channelData))
                    } catch (_: JSONException) {
                    }
                }
                sportsChannelAdapter.submitList(sportsChannel)
            }

            override fun onError(error: String?) {}
        })
    }

    private fun getEnterChannel(url: String) {
        service?.getChannelData(url, object : OnDataResponse {
            override fun onResponse(response: JSONObject) {
                enterChannel.clear()
                for (i in 0 until response.length()) {
                    try {
                        val channelData = response.getJSONObject(i.toString())
                        enterChannel.add(parseChannel(channelData))
                    } catch (_: JSONException) {
                    }
                }
                enterChannelAdapter.submitList(enterChannel)
            }

            override fun onError(error: String?) {}
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}