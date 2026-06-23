package com.flatcode.simplemultiapps.MultipleDelete.Activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.flatcode.simplemultiapps.MultipleDelete.Adapter.MultiDeleteAdapter
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.Unit.THEME
import com.flatcode.simplemultiapps.databinding.ActivityMultiDeleteBinding

class MultiDeleteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMultiDeleteBinding
    var arrayList = ArrayList<String>()
    var adapter: MultiDeleteAdapter? = null
    var activity: Activity? = null
    var context: Context = also { activity = it }

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)

        binding = ActivityMultiDeleteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        arrayList.addAll(resources.getStringArray(R.array.values))
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = MultiDeleteAdapter(context, activity, arrayList, binding.tvEmpty)
        binding.recyclerView.adapter = adapter
    }
}