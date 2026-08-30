@file:Suppress("SpellCheckingInspection")
package com.flatcode.simplemultiapps.livetv.activity

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.flatcode.simplemultiapps.livetv.model.Channel
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.databinding.ActivityLiveTvDetailsBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource

class LiveTVDetailsActivity : AppCompatActivity() {

    private var _binding: ActivityLiveTvDetailsBinding? = null
    private val binding get() = _binding!!

    private var fullScreen: ImageView? = null
    private var isFullScreen = false
    private var player: ExoPlayer? = null
    private var liveChannelUrl: String? = null
    val context: Context = this@LiveTVDetailsActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        _binding = ActivityLiveTvDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        @Suppress("DEPRECATION")
        val channel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("channel", Channel::class.java)
        } else {
            intent.getSerializableExtra("channel") as? Channel
        }

        if (channel != null) {
            binding.toolbar.nameSpace.text = channel.name
            binding.description.text = channel.description
            liveChannelUrl = channel.liveUrl

            binding.facebookLink.setOnClickListener { openLink(channel.facebook) }
            binding.twitterLink.setOnClickListener { openLink(channel.twitter) }
            binding.youtubeLink.setOnClickListener { openLink(channel.youtube) }
            binding.websiteLink.setOnClickListener { openLink(channel.website) }
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

    private fun openLink(url: String?) {
        if (!url.isNullOrEmpty()) {
            val open = Intent(Intent.ACTION_VIEW, url.toUri())
            startActivity(open)
        }
    }

    private fun initializePlayer() {
        val url = liveChannelUrl ?: return

        player = ExoPlayer.Builder(this).build().apply {
            binding.playerView.player = this
            val dataSourceFactory = DefaultHttpDataSource.Factory()
            val mediaSource = HlsMediaSource.Factory(dataSourceFactory).createMediaSource(
                MediaItem.fromUri(url.toUri())
            )
            setMediaSource(mediaSource)
            prepare()
            playWhenReady = true
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    when (state) {
                        Player.STATE_READY -> {
                            binding.progressBar.visibility = View.GONE
                        }
                        Player.STATE_BUFFERING -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.playerView.keepScreenOn = true
                        }
                        else -> {
                            binding.progressBar.visibility = View.GONE
                        }
                    }
                }
            })
        }
    }

    private fun releasePlayer() {
        player?.let {
            it.release()
            player = null
        }
    }

    override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    override fun onResume() {
        super.onResume()
        if (player == null) {
            initializePlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        // no-op for SDK >= 24 as per previous logic (releasePlayer in onStop)
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}