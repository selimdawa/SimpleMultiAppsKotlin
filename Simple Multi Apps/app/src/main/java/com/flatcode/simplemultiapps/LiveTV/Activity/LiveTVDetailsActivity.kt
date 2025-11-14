package com.flatcode.simplemultiapps.LiveTV.Activity

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.flatcode.simplemultiapps.LiveTV.Model.Channel
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.Unit.THEME
import com.flatcode.simplemultiapps.databinding.ActivityLiveTvDetailsBinding
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory

class LiveTVDetailsActivity : AppCompatActivity() {

    private var binding: ActivityLiveTvDetailsBinding? = null
    var fullScreen: ImageView? = null
    var isFullScreen = false
    var player: SimpleExoPlayer? = null
    var context: Context = this@LiveTVDetailsActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityLiveTvDetailsBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        val channel = intent.getSerializableExtra("channel") as Channel?
        binding!!.toolbar.nameSpace.text = channel!!.name
        binding!!.toolbar.back.setOnClickListener { onBackPressed() }
        fullScreen = binding!!.playerView.findViewById(R.id.exo_fullscreen_icon)

        fullScreen!!.setOnClickListener {
            if (isFullScreen) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                if (supportActionBar != null) {
                    supportActionBar!!.show()
                }
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                val params = binding!!.playerView.layoutParams as ConstraintLayout.LayoutParams
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                params.height = (200 * applicationContext.resources.displayMetrics.density).toInt()
                binding!!.playerView.layoutParams = params

                //Toast.makeText(Details.this, "We are Now going back to normal mode.", Toast.LENGTH_SHORT).show();
                isFullScreen = false
            } else {
                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
                if (supportActionBar != null) {
                    supportActionBar!!.hide()
                }
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                val params = binding!!.playerView.layoutParams as ConstraintLayout.LayoutParams
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                params.height = ViewGroup.LayoutParams.MATCH_PARENT
                binding!!.playerView.layoutParams = params

                //Toast.makeText(Details.this, "We are going to FullScreen Mode.", Toast.LENGTH_SHORT).show();
                isFullScreen = true
            }
        }
        binding!!.description.text = channel.description
        binding!!.facebookLink.setOnClickListener { openLink(channel.facebook) }
        binding!!.twitterLink.setOnClickListener { openLink(channel.twitter) }
        binding!!.youtubeLink.setOnClickListener { openLink(channel.youtube) }
        binding!!.websiteLink.setOnClickListener { openLink(channel.website) }

        playChannel(channel.live_url)
    }

    fun openLink(url: String?) {
        val open = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(open)
    }

    fun playChannel(live_url: String?) {
        player = SimpleExoPlayer.Builder(this).build()
        binding!!.playerView.player = player
        val dataSourceFactory: DataSource.Factory = DefaultHttpDataSourceFactory()
        val mediaSource =
            HlsMediaSource.Factory(dataSourceFactory).createMediaSource(
                MediaItem.fromUri(
                    live_url!!
                )
            )
        player!!.setMediaSource(mediaSource)
        player!!.prepare()
        player!!.playWhenReady = true
        player!!.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                when (state) {
                    Player.STATE_READY -> {
                        binding!!.progressBar.visibility = View.GONE
                        player!!.playWhenReady = true
                    }

                    Player.STATE_BUFFERING -> {
                        binding!!.progressBar.visibility = View.VISIBLE
                        binding!!.playerView.keepScreenOn = true
                    }

                    else -> {
                        binding!!.progressBar.visibility = View.GONE
                        player!!.playWhenReady = true
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        player!!.seekToDefaultPosition()
        player!!.playWhenReady = true
    }

    override fun onPause() {
        player!!.playWhenReady = false
        super.onPause()
    }

    override fun onDestroy() {
        player!!.release()
        super.onDestroy()
    }
}