package com.flatcode.simplemultiapps.multipledelete.activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.flatcode.simplemultiapps.multipledelete.adapter.MultiDeleteAdapter
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.utils.THEME
import com.flatcode.simplemultiapps.databinding.ActivityMultiDeleteBinding

class MultiDeleteActivity : AppCompatActivity() {

    private var _binding: ActivityMultiDeleteBinding? = null
    private val binding get() = _binding!!

    private val arrayList = ArrayList<String>()
    private var adapter: MultiDeleteAdapter? = null

    private val activity: Activity = this@MultiDeleteActivity
    private val context: Context = this@MultiDeleteActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        _binding = ActivityMultiDeleteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        arrayList.addAll(resources.getStringArray(R.array.values))

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