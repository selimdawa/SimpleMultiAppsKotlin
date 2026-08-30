package com.flatcode.simplemultiapps.jokeapp.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.flatcode.simplemultiapps.jokeapp.adapter.JokeCategoriesAdapter
import com.flatcode.simplemultiapps.jokeapp.fragment.JokesFragment
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.databinding.ActivityJokeAppBinding

class JokeAppActivity : AppCompatActivity() {

    private var _binding: ActivityJokeAppBinding? = null
    private val binding get() = _binding!!

    private var catAdapter: JokeCategoriesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        _binding = ActivityJokeAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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