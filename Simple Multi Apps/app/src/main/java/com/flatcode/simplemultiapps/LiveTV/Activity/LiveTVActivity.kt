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

    private var binding: ActivityLiveTvBinding? = null
    var bigSliderAdapter: ChannelAdapter? = null
    var newsChannelAdapter: ChannelAdapter? = null
    var sportsChannelAdapter: ChannelAdapter? = null
    var enterChannelAdapter: ChannelAdapter? = null
    var channelList: MutableList<Channel>? = null
    var newsChannels: MutableList<Channel>? = null
    var sportsChannel: MutableList<Channel>? = null
    var enterChannel: MutableList<Channel>? = null
    var service: ChannelDataService? = null
    var context: Context = this@LiveTVActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityLiveTvBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        binding!!.toolbar.nameSpace.setText(R.string.live_tv)
        channelList = ArrayList()
        service = ChannelDataService(this)

        binding!!.bigSliderList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        bigSliderAdapter = ChannelAdapter(channelList!!, "slider")
        binding!!.bigSliderList.adapter = bigSliderAdapter
        getSliderData("http://" + DATA.IP_LIVE_TV + "/mytv/api.php?key=1A4mgi2rBHCJdqggsYVx&id=1&channels=all")
        getNewsChannels("http://" + DATA.IP_LIVE_TV + "/mytv/api.php?key=1A4mgi2rBHCJdqggsYVx&id=1&cat=News")
        getSportsChannel("http://" + DATA.IP_LIVE_TV + "/mytv/api.php?key=1A4mgi2rBHCJdqggsYVx&id=1&cat=Sports")
        getEnterChannel("http://" + DATA.IP_LIVE_TV + "/mytv/api.php?key=1A4mgi2rBHCJdqggsYVx&id=1&cat=Entertainment")
        binding!!.toolbar.categories.setOnClickListener {
            VOID.Intent1(
                context,
                CLASS.LIVE_TV_CATEGORIES
            )
        }
        binding!!.more.setOnClickListener { v: View ->
            val i = Intent(v.context, CLASS.LIVE_TV_CATEGORIES_DETAILS)
            i.putExtra("categoryName", "News")
            v.context.startActivity(i)
        }
        binding!!.more2.setOnClickListener { v: View ->
            val i = Intent(v.context, CLASS.LIVE_TV_CATEGORIES_DETAILS)
            i.putExtra("categoryName", "Sports")
            v.context.startActivity(i)
        }
        binding!!.more3.setOnClickListener { v: View ->
            val i = Intent(v.context, CLASS.LIVE_TV_CATEGORIES_DETAILS)
            i.putExtra("categoryName", "Entertainment")
            v.context.startActivity(i)
        }
    }

    fun getSliderData(url: String?) {
        service!!.getChannelData(url, object : OnDataResponse {
            override fun onResponse(response: JSONObject) {
                for (i in 0 until response.length()) {
                    try {
                        val channelData = response.getJSONObject(i.toString())
                        val c = Channel()
                        c.id = channelData.getInt("id")
                        c.name = channelData.getString("name")
                        c.description = channelData.getString("description")
                        c.thumbnail = channelData.getString("thumbnail")
                        c.live_url = channelData.getString("live_url")
                        c.facebook = channelData.getString("facebook")
                        c.twitter = channelData.getString("twitter")
                        c.youtube = channelData.getString("youtube")
                        c.website = channelData.getString("website")
                        c.category = channelData.getString("category")
                        channelList!!.add(c)
                        bigSliderAdapter!!.notifyDataSetChanged()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onError(error: String?) {}
        })
    }

    fun getNewsChannels(url: String?) {
        newsChannels = ArrayList()
        binding!!.newsChannelList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        newsChannelAdapter = ChannelAdapter(newsChannels!!, "category")
        binding!!.newsChannelList.adapter = newsChannelAdapter
        service!!.getChannelData(url, object : OnDataResponse {
            override fun onResponse(response: JSONObject) {
                for (i in 0 until response.length()) {
                    try {
                        val channelData = response.getJSONObject(i.toString())
                        val c = Channel()
                        c.id = channelData.getInt("id")
                        c.name = channelData.getString("name")
                        c.description = channelData.getString("description")
                        c.thumbnail = channelData.getString("thumbnail")
                        c.live_url = channelData.getString("live_url")
                        c.facebook = channelData.getString("facebook")
                        c.twitter = channelData.getString("twitter")
                        c.youtube = channelData.getString("youtube")
                        c.website = channelData.getString("website")
                        c.category = channelData.getString("category")
                        newsChannels!!.add(c)
                        newsChannelAdapter!!.notifyDataSetChanged()

                        //Log.d(TAG, "onResponse: " + c.toString());
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onError(error: String?) {}
        })
    }

    fun getSportsChannel(url: String?) {
        sportsChannel = ArrayList()
        binding!!.sportsChannelList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        sportsChannelAdapter = ChannelAdapter(sportsChannel!!, "category")
        binding!!.sportsChannelList.adapter = sportsChannelAdapter
        service!!.getChannelData(url, object : OnDataResponse {
            override fun onResponse(response: JSONObject) {
                //Log.d(TAG, "onResponse: sports" + response.toString());
                for (i in 0 until response.length()) {
                    try {
                        val channelData = response.getJSONObject(i.toString())
                        val c = Channel()
                        c.id = channelData.getInt("id")
                        c.name = channelData.getString("name")
                        c.description = channelData.getString("description")
                        c.thumbnail = channelData.getString("thumbnail")
                        c.live_url = channelData.getString("live_url")
                        c.facebook = channelData.getString("facebook")
                        c.twitter = channelData.getString("twitter")
                        c.youtube = channelData.getString("youtube")
                        c.website = channelData.getString("website")
                        c.category = channelData.getString("category")
                        sportsChannel!!.add(c)
                        sportsChannelAdapter!!.notifyDataSetChanged()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onError(error: String?) {}
        })
    }

    fun getEnterChannel(url: String?) {
        enterChannel = ArrayList()
        binding!!.enterChannelList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        enterChannelAdapter = ChannelAdapter(enterChannel!!, "category")
        binding!!.enterChannelList.adapter = enterChannelAdapter
        service!!.getChannelData(url, object : OnDataResponse {
            override fun onResponse(response: JSONObject) {
                for (i in 0 until response.length()) {
                    try {
                        val channelData = response.getJSONObject(i.toString())
                        val c = Channel()
                        c.id = channelData.getInt("id")
                        c.name = channelData.getString("name")
                        c.description = channelData.getString("description")
                        c.thumbnail = channelData.getString("thumbnail")
                        c.live_url = channelData.getString("live_url")
                        c.facebook = channelData.getString("facebook")
                        c.twitter = channelData.getString("twitter")
                        c.youtube = channelData.getString("youtube")
                        c.website = channelData.getString("website")
                        c.category = channelData.getString("category")
                        enterChannel!!.add(c)
                        enterChannelAdapter!!.notifyDataSetChanged()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onError(error: String?) {}
        })
    }
}