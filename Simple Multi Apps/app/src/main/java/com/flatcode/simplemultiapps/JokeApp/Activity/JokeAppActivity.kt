package com.flatcode.simplemultiapps.jokeapp.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.flatcode.simplemultiapps.jokeapp.adapter.JokeCategoriesAdapter
import com.flatcode.simplemultiapps.jokeapp.fragment.JokesFragment
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.databinding.ActivityJokeAppBinding

class JokeAppActivity : AppCompatActivity() {

    private var _binding: ActivityJokeAppBinding? = null
    private val binding get() = _binding!!

    private var catAdapter: JokeCategoriesAdapter? = null
    private val context: Context = this@JokeAppActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityJokeAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.nameSpace.setText(R.string.joke)

        val cats = listOf("Any", "Programming", "Dark", "Spooky", "Misc", "Pun", "Christmas")

        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            catAdapter = JokeCategoriesAdapter(context, cats)
            adapter = catAdapter
        }

        if (savedInstanceState == null) {
            val fragment = JokesFragment().apply {
                arguments = Bundle().apply {
                    putString(
                        JokesFragment.KEY_JOKES_URL,
                        "https://v2.jokeapi.dev/joke/Any?amount=10"
                    )
                }
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, fragment)
                .commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}