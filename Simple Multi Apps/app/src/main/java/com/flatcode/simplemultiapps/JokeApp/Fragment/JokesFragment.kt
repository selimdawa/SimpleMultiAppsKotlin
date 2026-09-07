package com.flatcode.simplemultiapps.jokeapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.flatcode.simplemultiapps.databinding.FragmentJokesBinding
import com.flatcode.simplemultiapps.jokeapp.adapter.JokeAdapter
import com.flatcode.simplemultiapps.jokeapp.model.Joke
import com.flatcode.simplemultiapps.utils.DATA
import org.json.JSONException

class JokesFragment : Fragment() {

    private var _binding: FragmentJokesBinding? = null
    private val binding get() = _binding!!

    private val jokes = ArrayList<Joke>()
    private var adapter: JokeAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentJokesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = JokeAdapter(jokes)

        binding.jokesList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@JokesFragment.adapter
        }

        arguments?.getString(KEY_JOKES_URL)?.let { url ->
            getJokes(url)
        }
    }

    private fun getJokes(url: String) {
        val queue = Volley.newRequestQueue(requireContext())
        val objectRequest = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            try {
                val jokesArray = response.getJSONArray(DATA.JOKES)
                val amount = response.optInt(DATA.AMOUNT, jokesArray.length())

                for (i in 0 until amount) {
                    val jokeData = jokesArray.getJSONObject(i)
                    val jokeType = jokeData.optString(DATA.TYPE)

                    val jokeObject = Joke().apply {
                        type = jokeType
                        if (jokeType == DATA.JOKE_TYPE_SINGLE) {
                            joke = jokeData.optString(DATA.JOKE)
                        } else {
                            setup = jokeData.optString(DATA.SETUP)
                            delivery = jokeData.optString(DATA.DELIVERY)
                        }
                    }
                    jokes.add(jokeObject)
                }
                adapter?.notifyItemRangeInserted(jokes.size - amount, amount)
            } catch (_: JSONException) {
            }
        }, null)

        queue.add(objectRequest)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val KEY_JOKES_URL = DATA.JOKES_URL
    }
}