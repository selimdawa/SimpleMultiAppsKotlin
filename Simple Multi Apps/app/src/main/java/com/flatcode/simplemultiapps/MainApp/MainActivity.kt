package com.flatcode.simplemultiapps.mainapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.bloggerapp.activity.BloggerAppActivity
import com.flatcode.simplemultiapps.candycrushgame.CandyCrushGameActivity
import com.flatcode.simplemultiapps.jokeapp.activity.JokeAppActivity
import com.flatcode.simplemultiapps.livetv.activity.LiveTVActivity
import com.flatcode.simplemultiapps.multipledelete.activity.MultiDeleteActivity
import com.flatcode.simplemultiapps.newsapp.activity.NewsAppActivity
import com.flatcode.simplemultiapps.pdfreader.activity.PdfReaderActivity
import com.flatcode.simplemultiapps.randomimagegenerating.RandomImageGeneratingActivity
import com.flatcode.simplemultiapps.stopwatch.StopWatchActivity
import com.flatcode.simplemultiapps.videoplayer.activity.VideoPlayerActivity
import com.flatcode.simplemultiapps.webapp.WebAppActivity
import com.flatcode.simplemultiapps.wordpress.activity.WordpressActivity
import com.flatcode.simplemultiapps.utils.THEME
import com.flatcode.simplemultiapps.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnSharedPreferenceChangeListener {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val list = ArrayList<Main>()
    private var adapter: MainAdapter? = null
    val context: Context = this@MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        PreferenceManager.getDefaultSharedPreferences(baseContext)
            .registerOnSharedPreferenceChangeListener(this)
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()

        binding.recyclerView.setHasFixedSize(true)
        adapter = MainAdapter(context, list)
        binding.recyclerView.adapter = adapter

        ideaPosts(1, 1, 1, 2, 4, 1, 4, 2, 2, 3, 2, 3)
    }

    private fun ideaPosts(
        i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int,
        i7: Int, i8: Int, i9: Int, i10: Int, i11: Int, i12: Int
    ) {
        list.clear()
        list.addAll(
            listOf(
                Main(R.drawable.ic_stop_watch, "Stop Watch", i1, StopWatchActivity::class.java),
                Main(R.drawable.ic_candy_crush, "Candy Crush Game", i2, CandyCrushGameActivity::class.java),
                Main(R.drawable.ic_multi_delete, "Multiple Delete", i3, MultiDeleteActivity::class.java),
                Main(R.drawable.ic_random, "Random Image Generating", i4, RandomImageGeneratingActivity::class.java),
                Main(R.drawable.ic_blogger, "Blogger App", i5, BloggerAppActivity::class.java),
                Main(R.drawable.ic_joke, "Joke App", i6, JokeAppActivity::class.java),
                Main(R.drawable.ic_live_tv, "Live TV", i7, LiveTVActivity::class.java),
                Main(R.drawable.ic_news, "News App", i8, NewsAppActivity::class.java),
                Main(R.drawable.ic_pdf_reader, "Pdf Reader", i9, PdfReaderActivity::class.java),
                Main(R.drawable.ic_video_player, "Video Player", i10, VideoPlayerActivity::class.java),
                Main(R.drawable.ic_web, "Web App", i11, WebAppActivity::class.java),
                Main(R.drawable.ic_wordpress, "WordPress Blog", i12, WordpressActivity::class.java)
            )
        )
        adapter?.notifyItemRangeInserted(0, list.size)

        binding.bar.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == "color_option") {
            recreate()
        }
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SETTINGS_CODE) {
            recreate()
        }
    }

    override fun onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(baseContext)
            .unregisterOnSharedPreferenceChangeListener(this)
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val SETTINGS_CODE = 234
    }
}