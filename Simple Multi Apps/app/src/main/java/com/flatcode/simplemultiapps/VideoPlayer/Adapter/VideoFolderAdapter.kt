package com.flatcode.simplemultiapps.videoplayer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.decode.VideoFrameDecoder
import coil.load
import com.flatcode.simplemultiapps.utils.formatDuration
import com.flatcode.simplemultiapps.utils.intent1
import com.flatcode.simplemultiapps.videoplayer.activity.PlayerActivity
import com.flatcode.simplemultiapps.videoplayer.model.VideoFiles
import com.flatcode.simplemultiapps.databinding.ItemVideoBinding
import java.io.File

class VideoFolderAdapter(
    private val context: Context,
    private val folderVideoFiles: ArrayList<VideoFiles?>,
) : RecyclerView.Adapter<VideoFolderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentVideo = folderVideoFiles[position] ?: return

        with(holder.binding) {
            name.text = currentVideo.title

            val durationMs = currentVideo.duration?.toLongOrNull() ?: 0L
            duration.text = durationMs.formatDuration()

            currentVideo.path?.let { path ->
                image.load(File(path)) {
                    decoderFactory { result, options, _ ->
                        VideoFrameDecoder(result.source, options)
                    }
                }
            }

            root.setOnClickListener {
                context.intent1(PlayerActivity::class.java) {
                    putExtra("position", position)
                    putExtra("sender", "FolderIsSending")
                }
            }
        }
    }

    override fun getItemCount(): Int = folderVideoFiles.size

    class ViewHolder(val binding: ItemVideoBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        var folderVideoFile: ArrayList<VideoFiles?>? = null
    }

    init {
        folderVideoFile = folderVideoFiles
    }
}