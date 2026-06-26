package com.flatcode.simplemultiapps.VideoPlayer.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.flatcode.simplemultiapps.Unit.CLASS
import com.flatcode.simplemultiapps.Unit.VOID
import com.flatcode.simplemultiapps.VideoPlayer.Model.VideoFiles
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
            duration.text = VOID.convertDuration(durationMs)

            currentVideo.path?.let { path ->
                Glide.with(context)
                    .load(File(path))
                    .into(image)
            }

            root.setOnClickListener {
                val intent = Intent(context, CLASS.VIDEO_PLAY).apply {
                    putExtra("position", position)
                    putExtra("sender", "FolderIsSending")
                }
                context.startActivity(intent)
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