package com.flatcode.simplemultiapps.LiveTV.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.flatcode.simplemultiapps.LiveTV.Adapter.ChannelAdapter
import com.flatcode.simplemultiapps.LiveTV.Model.Channel
import com.flatcode.simplemultiapps.LiveTV.Service.ChannelDataService
import com.flatcode.simplemultiapps.LiveTV.Service.ChannelDataService.OnDataResponse
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.Unit.CLASS
import com.flatcode.simplemultiapps.Unit.DATA
import com.flatcode.simplemultiapps.Unit.THEME
import com.flatcode.simplemultiapps.Unit.VOID
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
    var context: Context = this@LiveTVActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        _binding = ActivityLiveTvBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.nameSpace.setText(R.string.live_tv)
        service = ChannelDataService(this)

        setupRecyclerViews()
        setupClickListeners()
        loadAllChannels()
    }

    private fun setupRecyclerViews() {
        binding.bigSliderList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        bigSliderAdapter = ChannelAdapter("slider")
        binding.bigSliderList.adapter = bigSliderAdapter

        binding.newsChannelList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        newsChannelAdapter = ChannelAdapter("details")
        binding.newsChannelList.adapter = newsChannelAdapter

        binding.sportsChannelList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        sportsChannelAdapter = ChannelAdapter("details")
        binding.sportsChannelList.adapter = sportsChannelAdapter

        binding.enterChannelList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        enterChannelAdapter = ChannelAdapter("details")
        binding.enterChannelList.adapter = enterChannelAdapter
    }

    private fun setupClickListeners() {
        binding.toolbar.categories.setOnClickListener {
            VOID.Intent1(context, CLASS.LIVE_TV_CATEGORIES)
        }
        binding.more.setOnClickListener { v: View ->
            startCategoryDetailActivity(v.context, "News")
        }
        binding.more2.setOnClickListener { v: View ->
            startCategoryDetailActivity(v.context, "Sports")
        }
        binding.more3.setOnClickListener { v: View ->
            startCategoryDetailActivity(v.context, "Entertainment")
        }
    }

    private fun startCategoryDetailActivity(ctx: Context, categoryName: String) {
        val i = Intent(ctx, CLASS.LIVE_TV_CATEGORIES_DETAILS)
        i.putExtra("categoryName", categoryName)
        ctx.startActivity(i)
    }

    private fun loadAllChannels() {
        getSliderData("http://${DATA.IP_LIVE_TV}/mytv/api.php?key=1A4mgi2rBHCJdqggsYVx&id=1&channels=all")
        getNewsChannels("http://${DATA.IP_LIVE_TV}/mytv/api.php?key=1A4mgi2rBHCJdqggsYVx&id=1&cat=News")
        getSportsChannel("http://${DATA.IP_LIVE_TV}/mytv/api.php?key=1A4mgi2rBHCJdqggsYVx&id=1&cat=Sports")
        getEnterChannel("http://${DATA.IP_LIVE_TV}/mytv/api.php?key=1A4mgi2rBHCJdqggsYVx&id=1&cat=Entertainment")
    }

    private fun parseChannel(channelData: JSONObject): Channel {
        return Channel(
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
    }

    fun getSliderData(url: String?) {
        service?.getChannelData(url, object : OnDataResponse {
            override fun onResponse(response: JSONObject) {
                for (i in 0 until response.length()) {
                    try {
                        val channelData = response.getJSONObject(i.toString())
                        channelList.add(parseChannel(channelData))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                bigSliderAdapter.submitList(ArrayList(channelList))
            }

            override fun onError(error: String?) {}
        })
    }

    fun getNewsChannels(url: String?) {
        service?.getChannelData(url, object : OnDataResponse {
            override fun onResponse(response: JSONObject) {
                for (i in 0 until response.length()) {
                    try {
                        val channelData = response.getJSONObject(i.toString())
                        newsChannels.add(parseChannel(channelData))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                newsChannelAdapter.submitList(ArrayList(newsChannels))
            }

            override fun onError(error: String?) {}
        })
    }

    fun getSportsChannel(url: String?) {
        service?.getChannelData(url, object : OnDataResponse {
            override fun onResponse(response: JSONObject) {
                for (i in 0 until response.length()) {
                    try {
                        val channelData = response.getJSONObject(i.toString())
                        sportsChannel.add(parseChannel(channelData))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                sportsChannelAdapter.submitList(ArrayList(sportsChannel))
            }

            override fun onError(error: String?) {}
        })
    }

    fun getEnterChannel(url: String?) {
        service?.getChannelData(url, object : OnDataResponse {
            override fun onResponse(response: JSONObject) {
                for (i in 0 until response.length()) {
                    try {
                        val channelData = response.getJSONObject(i.toString())
                        enterChannel.add(parseChannel(channelData))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                enterChannelAdapter.submitList(ArrayList(enterChannel))
            }

            override fun onError(error: String?) {}
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}