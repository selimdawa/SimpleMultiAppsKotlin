package com.flatcode.simplemultiapps.mainapp

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.bloggerapp.activity.BloggerAppActivity
import com.flatcode.simplemultiapps.candycrushgame.CandyCrushGameActivity
import com.flatcode.simplemultiapps.databinding.ActivityMainBinding
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

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val list = ArrayList<Main>()
    private var adapter: MainAdapter? = null
    val context: Context = this@MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.recyclerView.setHasFixedSize(true)
        adapter = MainAdapter(context, list)
        binding.recyclerView.adapter = adapter

        ideaPosts()
    }

    private fun ideaPosts() {
        list.clear()
        list.addAll(
            listOf(
                Main(R.drawable.ic_stop_watch, getString(R.string.stop_watch), 1, StopWatchActivity::class.java),
                Main(
                    R.drawable.ic_candy_crush,
                    getString(R.string.candy_crush_game),
                    1,
                    CandyCrushGameActivity::class.java,
                ),
                Main(
                    R.drawable.ic_multi_delete,
                    getString(R.string.multi_delete),
                    1,
                    MultiDeleteActivity::class.java,
                ),
                Main(
                    R.drawable.ic_random,
                    getString(R.string.random_image_generating),
                    2,
                    RandomImageGeneratingActivity::class.java,
                ),
                Main(R.drawable.ic_blogger, getString(R.string.blogger_app), 4, BloggerAppActivity::class.java),
                Main(R.drawable.ic_joke, getString(R.string.joke), 1, JokeAppActivity::class.java),
                Main(R.drawable.ic_live_tv, getString(R.string.live_tv), 4, LiveTVActivity::class.java),
                Main(R.drawable.ic_news, getString(R.string.news_app), 2, NewsAppActivity::class.java),
                Main(R.drawable.ic_pdf_reader, getString(R.string.pdf_reader), 2, PdfReaderActivity::class.java),
                Main(
                    R.drawable.ic_video_player, getString(R.string.video_player), 3, VideoPlayerActivity::class.java,
                ),
                Main(R.drawable.ic_web, getString(R.string.web_app), 2, WebAppActivity::class.java),
                Main(R.drawable.ic_wordpress, getString(R.string.wordpress_blog), 3, WordpressActivity::class.java),
            )
        )
        adapter?.notifyItemRangeInserted(0, list.size)

        binding.bar.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}