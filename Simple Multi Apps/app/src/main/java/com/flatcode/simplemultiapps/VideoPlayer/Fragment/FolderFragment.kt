package com.flatcode.simplemultiapps.VideoPlayer.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.flatcode.simplemultiapps.VideoPlayer.Activity.VideoPlayerActivity.Companion.folderList
import com.flatcode.simplemultiapps.VideoPlayer.Activity.VideoPlayerActivity.Companion.videoFiles
import com.flatcode.simplemultiapps.VideoPlayer.Adapter.FolderAdapter
import com.flatcode.simplemultiapps.databinding.FragmentFolderBinding

class FolderFragment : Fragment() {

    private var _binding: FragmentFolderBinding? = null
    private val binding get() = _binding!!

    private var adapter: FolderAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFolderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentFolders = folderList
        val currentVideos = videoFiles

        if (!currentFolders.isNullOrEmpty() && currentVideos != null) {
            adapter = FolderAdapter(requireContext(), currentVideos, currentFolders)

            binding.recyclerView.apply {
                adapter = this@FolderFragment.adapter
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}