package com.flatcode.simplemultiapps.LiveTV.Activity

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.flatcode.simplemultiapps.LiveTV.Model.Channel
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.Unit.THEME
import com.flatcode.simplemultiapps.databinding.ActivityLiveTvDetailsBinding
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource

class LiveTVDetailsActivity : AppCompatActivity() {

    private var _binding: ActivityLiveTvDetailsBinding? = null
    private val binding get() = _binding!!

    var fullScreen: ImageView? = null
    var isFullScreen = false
    var player: SimpleExoPlayer? = null
    var context: Context = this@LiveTVDetailsActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        _binding = ActivityLiveTvDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        val channel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("channel", Channel::class.java)
        } else {
            intent.getSerializableExtra("channel") as? Channel
        }

        if (channel != null) {
            binding.toolbar.nameSpace.text = channel.name
            binding.description.text = channel.description

            binding.facebookLink.setOnClickListener { openLink(channel.facebook) }
            binding.twitterLink.setOnClickListener { openLink(channel.twitter) }
            binding.youtubeLink.setOnClickListener { openLink(channel.youtube) }
            binding.websiteLink.setOnClickListener { openLink(channel.website) }

            playChannel(channel.liveUrl)
        }

        binding.toolbar.back.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        fullScreen = binding.playerView.findViewById(R.id.exo_fullscreen_icon)

        fullScreen?.setOnClickListener {
            val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

            if (isFullScreen) {
                windowInsetsController.show(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars())
                supportActionBar?.show()
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

                val params = binding.playerView.layoutParams as ConstraintLayout.LayoutParams
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                params.height = (200 * applicationContext.resources.displayMetrics.density).toInt()
                binding.playerView.layoutParams = params
                isFullScreen = false
            } else {
                windowInsetsController.hide(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars())
                windowInsetsController.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                supportActionBar?.hide()
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

                val params = binding.playerView.layoutParams as ConstraintLayout.LayoutParams
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                params.height = ViewGroup.LayoutParams.MATCH_PARENT
                binding.playerView.layoutParams = params
                isFullScreen = true
            }
        }
    }

    fun openLink(url: String?) {
        if (!url.isNullOrEmpty()) {
            val open = Intent(Intent.ACTION_VIEW, url.toUri())
            startActivity(open)
        }
    }

    fun playChannel(liveUrl: String?) {
        if (liveUrl.isNullOrEmpty()) return

        player = SimpleExoPlayer.Builder(this).build()
        binding.playerView.player = player
        val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()
        val mediaSource = HlsMediaSource.Factory(dataSourceFactory).createMediaSource(
            MediaItem.fromUri(liveUrl.toUri())
        )
        player?.setMediaSource(mediaSource)
        player?.prepare()
        player?.playWhenReady = true
        player?.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                when (state) {
                    Player.STATE_READY -> {
                        binding.progressBar.visibility = View.GONE
                        player?.playWhenReady = true
                    }

                    Player.STATE_BUFFERING -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.playerView.keepScreenOn = true
                    }

                    else -> {
                        binding.progressBar.visibility = View.GONE
                        player?.playWhenReady = true
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        player?.seekToDefaultPosition()
        player?.playWhenReady = true
    }

    override fun onPause() {
        player?.playWhenReady = false
        super.onPause()
    }

    override fun onDestroy() {
        player?.release()
        super.onDestroy()
        _binding = null
    }
}