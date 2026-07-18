package com.flatcode.simplemultiapps.videoplayer.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.flatcode.simplemultiapps.utils.CLASS
import com.flatcode.simplemultiapps.utils.VOID
import com.flatcode.simplemultiapps.videoplayer.model.VideoFiles
import com.flatcode.simplemultiapps.databinding.ItemVideoBinding
import java.io.File

class VideoAdapter(
    private val context: Context,
    private val videoFiles: ArrayList<VideoFiles?>
) : RecyclerView.Adapter<VideoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentVideo = videoFiles[position] ?: return

        with(holder.binding) {
            name.text = currentVideo.title

            val durationMs = currentVideo.duration?.toLongOrNull() ?: 0L
            duration.text = VOID.convertDuration(durationMs)

            currentVideo.path?.let { path ->
                image.load(File(path))
            }

            root.setOnClickListener {
                val intent = Intent(context, CLASS.VIDEO_PLAY).apply {
                    putExtra("position", position)
                    putExtra("sender", "FilesIsSending")
                }
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = videoFiles.size

    class ViewHolder(val binding: ItemVideoBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        var videoFile: ArrayList<VideoFiles?>? = null
    }

    init {
        videoFile = videoFiles
    }
}