package com.flatcode.simplemultiapps.JokeApp.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.flatcode.simplemultiapps.JokeApp.Adapter.JokeAdapter
import com.flatcode.simplemultiapps.JokeApp.Model.Joke
import com.flatcode.simplemultiapps.databinding.FragmentJokesBinding
import org.json.JSONException

class JokesFragment : Fragment() {

    private var _binding: FragmentJokesBinding? = null
    private val binding get() = _binding!!

    private val jokes = ArrayList<Joke>()
    private var adapter: JokeAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
                val jokesArray = response.getJSONArray("jokes")
                val amount = response.optInt("amount", jokesArray.length())

                for (i in 0 until amount) {
                    val jokeData = jokesArray.getJSONObject(i)
                    val jokeType = jokeData.optString("type")

                    val jokeObject = Joke().apply {
                        type = jokeType
                        if (jokeType == "single") {
                            joke = jokeData.optString("joke")
                        } else {
                            setup = jokeData.optString("setup")
                            delivery = jokeData.optString("delivery")
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
        const val KEY_JOKES_URL = "extra_jokes_url"
    }
}