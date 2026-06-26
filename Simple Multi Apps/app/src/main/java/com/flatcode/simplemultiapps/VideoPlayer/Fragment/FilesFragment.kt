package com.flatcode.simplemultiapps.VideoPlayer.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.flatcode.simplemultiapps.VideoPlayer.Activity.VideoPlayerActivity.Companion.videoFiles
import com.flatcode.simplemultiapps.VideoPlayer.Adapter.VideoAdapter
import com.flatcode.simplemultiapps.databinding.FragmentFilesBinding

class FilesFragment : Fragment() {

    private var _binding: FragmentFilesBinding? = null
    private val binding get() = _binding!!

    private var adapter: VideoAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val files = videoFiles
        if (!files.isNullOrEmpty()) {
            adapter = VideoAdapter(requireContext(), files)

            binding.recyclerView.apply {
                adapter = this@FilesFragment.adapter
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}