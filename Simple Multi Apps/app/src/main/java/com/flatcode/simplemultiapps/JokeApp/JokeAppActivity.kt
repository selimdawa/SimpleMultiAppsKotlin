package com.flatcode.simplemultiapps.JokeApp

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.Unit.THEME
import com.flatcode.simplemultiapps.databinding.ActivityJokeAppBinding

class JokeAppActivity : AppCompatActivity() {

    private var binding: ActivityJokeAppBinding? = null
    var jokeList: RecyclerView? = null
    var catAdapter: JokeCategoriesAdapter? = null
    var context: Context = this@JokeAppActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityJokeAppBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        binding!!.toolbar.nameSpace.setText(R.string.joke)

        val cats = ArrayList<String>()
        cats.add("Any")
        cats.add("Programming")
        cats.add("Dark")
        cats.add("Spooky")
        cats.add("Misc")
        cats.add("Pun")
        cats.add("Christmas")

        binding!!.recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        catAdapter = JokeCategoriesAdapter(context, cats)
        binding!!.recyclerView.adapter = catAdapter
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction().replace(
            R.id.fragment, JokesFragment("https://v2.jokeapi.dev/joke/Any?amount=10")
        )
        transaction.commit()
    }
}