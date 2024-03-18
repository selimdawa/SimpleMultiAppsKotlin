package com.flatcode.simplemultiapps.MultipleDelete

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.flatcode.simplemultiapps.Unit.THEME
import com.flatcode.simplemultiapps.databinding.ActivityMultiDeleteBinding

class MultiDeleteActivity : AppCompatActivity() {

    private var binding: ActivityMultiDeleteBinding? = null
    var arrayList = ArrayList<String>()
    var adapter: MultiDeleteAdapter? = null
    var activity: Activity? = null
    var context: Context = also { activity = it }

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityMultiDeleteBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        //Add values in array list
        arrayList.addAll(
            listOf(
                "one",
                "two",
                "three",
                "four",
                "five",
                "sex",
                "seven",
                "eight",
                "nine",
                "ten",
                "eleven",
                "twelve",
                "thirteen",
                "fourteen",
                "fifteen"
            )
        )

        //Set layout manager
        binding!!.recyclerView.layoutManager = LinearLayoutManager(context)
        //Initialize adapter
        adapter = MultiDeleteAdapter(context, activity, arrayList, binding!!.tvEmpty)
        //Set adapter
        binding!!.recyclerView.adapter = adapter
    }
}