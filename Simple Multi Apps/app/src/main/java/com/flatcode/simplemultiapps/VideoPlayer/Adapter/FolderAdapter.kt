package com.flatcode.simplemultiapps.videoplayer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.flatcode.simplemultiapps.utils.intent1
import com.flatcode.simplemultiapps.videoplayer.activity.VideoFolderActivity
import com.flatcode.simplemultiapps.videoplayer.model.VideoFiles
import com.flatcode.simplemultiapps.databinding.ItemVideoPlayerFolderBinding

class FolderAdapter(
    private val context: Context,
    private val videoFiles: ArrayList<VideoFiles?>?,
    private val folderName: ArrayList<String>,
) : RecyclerView.Adapter<FolderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemVideoPlayerFolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentPath = folderName[position]

        with(holder.binding) {
            name.text = currentPath.substringAfterLast('/')
            count.text = numberOfFiles(currentPath).toString()

            root.setOnClickListener {
                context.intent1(VideoFolderActivity::class.java) {
                    putExtra("folderName", currentPath)
                }
            }
        }
    }

    override fun getItemCount(): Int = folderName.size

    private fun numberOfFiles(folderName: String): Int {
        if (videoFiles == null) return 0
        return videoFiles.count { file ->
            val path = file?.path ?: return@count false
            path.substringBeforeLast('/', "").endsWith(folderName)
        }
    }

    class ViewHolder(val binding: ItemVideoPlayerFolderBinding) :
        RecyclerView.ViewHolder(binding.root)
}