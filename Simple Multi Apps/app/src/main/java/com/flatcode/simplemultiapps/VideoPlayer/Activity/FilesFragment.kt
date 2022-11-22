package com.flatcode.simplemultiapps.VideoPlayer.Activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.VideoPlayer.Activity.VideoPlayerActivity.Companion.videoFiles
import com.flatcode.simplemultiapps.VideoPlayer.Adapter.VideoAdapter

class FilesFragment : Fragment() {

    var recyclerView: RecyclerView? = null
    var adapter: VideoAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_files, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        if (videoFiles != null && videoFiles!!.size > 0) {
            adapter = VideoAdapter(requireContext(), videoFiles!!)
            recyclerView!!.adapter = adapter
            recyclerView!!.layoutManager = LinearLayoutManager(context,
                RecyclerView.VERTICAL,
                false)
        }
        return view
    }
}