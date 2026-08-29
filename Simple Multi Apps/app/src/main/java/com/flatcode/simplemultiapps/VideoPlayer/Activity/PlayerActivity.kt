package com.flatcode.simplemultiapps.videoplayer.activity

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.videoplayer.adapter.VideoAdapter.Companion.videoFile
import com.flatcode.simplemultiapps.videoplayer.adapter.VideoFolderAdapter.Companion.folderVideoFile
import com.flatcode.simplemultiapps.videoplayer.model.VideoFiles
import com.flatcode.simplemultiapps.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {

    private var _binding: ActivityPlayerBinding? = null
    private val binding get() = _binding!!

    private val context: Context = this@PlayerActivity
    private var exoPlayer: ExoPlayer? = null
    private var position = -1
    private var myFiles: ArrayList<VideoFiles?>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        setFullScreen()
        super.onCreate(savedInstanceState)
        _binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        position = intent.getIntExtra("position", -1)
        val sender = intent.getStringExtra("sender")

        myFiles = if (sender == "FolderIsSending") folderVideoFile else videoFile

        if (myFiles.isNullOrEmpty() || position == -1) {
            Toast.makeText(context, R.string.data_not_found, Toast.LENGTH_SHORT).show()
            finish()
            return
        }
    }

    @OptIn(UnstableApi::class)
    private fun initializePlayer() {
        val path = myFiles?.getOrNull(position)?.path ?: return
        val uri = path.toUri()

        exoPlayer = ExoPlayer.Builder(context).build().apply {
            val dataSourceFactory = DefaultDataSource.Factory(context)
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(uri))

            binding.expo.player = this
            binding.expo.keepScreenOn = true

            setMediaSource(mediaSource)
            prepare()
            playWhenReady = true
        }
    }

    private fun releasePlayer() {
        exoPlayer?.let { player ->
            player.release()
            exoPlayer = null
        }
    }

    private fun setFullScreen() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT > 23) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT <= 23 || exoPlayer == null) {
            initializePlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Build.VERSION.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Build.VERSION.SDK_INT > 23) {
            releasePlayer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}