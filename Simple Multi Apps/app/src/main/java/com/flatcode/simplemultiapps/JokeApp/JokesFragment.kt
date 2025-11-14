package com.flatcode.simplemultiapps.JokeApp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.flatcode.simplemultiapps.R
import org.json.JSONException
import org.json.JSONObject

class JokesFragment(var jokesUrl: String) : Fragment() {

    var jokesList: RecyclerView? = null
    var adapter: JokeAdapter? = null
    var jokes: MutableList<Joke>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_jokes, container, false)

        jokesList = view.findViewById(R.id.jokesList)
        adapter = JokeAdapter(context, jokes)
        jokesList!!.layoutManager = LinearLayoutManager(context)
        jokesList!!.adapter = adapter
        getJokes(jokesUrl)

        return view
    }

    fun getJokes(url: String?) {
        // fetch jokes and populate the data
        val queue = Volley.newRequestQueue(requireContext())
        val objectRequest =
            JsonObjectRequest(Request.Method.GET, url, null, { response: JSONObject ->
                try {
                    val jokesArray = response.getJSONArray("jokes")
                    for (i in 0 until response.getString("amount").toInt()) {
                        val jokeData = jokesArray.getJSONObject(i)
                        val j = Joke()
                        j.type = (jokeData.getString("type"))
                        if (jokeData.getString("type") == "single") {
                            j.joke = (jokeData.getString("joke"))
                        } else {
                            j.setup = (jokeData.getString("setup"))
                            j.delivery = (jokeData.getString("delivery"))
                        }
                        jokes.add(j)
                        adapter!!.notifyDataSetChanged()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { _: VolleyError? -> }
        queue.add(objectRequest)
    }

    init {
        jokes = ArrayList()
    }
}