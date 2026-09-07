package com.flatcode.simplemultiapps.multipledelete.activity

import android.app.Activity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.flatcode.simplemultiapps.databinding.ActivityMultiDeleteBinding
import com.flatcode.simplemultiapps.multipledelete.adapter.MultiDeleteAdapter
import com.flatcode.simplemultiapps.utils.DATA

class MultiDeleteActivity : AppCompatActivity() {

    private var _binding: ActivityMultiDeleteBinding? = null
    private val binding get() = _binding!!

    private val arrayList = ArrayList<String>()
    private var adapter: MultiDeleteAdapter? = null

    private val activity: Activity = this@MultiDeleteActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        _binding = ActivityMultiDeleteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        arrayList.addAll(DATA.VALUES)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = MultiDeleteAdapter(context, activity, arrayList, binding.tvEmpty).also {
                this@MultiDeleteActivity.adapter = it
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}