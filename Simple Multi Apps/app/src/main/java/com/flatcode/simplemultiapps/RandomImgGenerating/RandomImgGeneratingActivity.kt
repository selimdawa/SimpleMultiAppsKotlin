package com.flatcode.simplemultiapps.RandomImgGenerating

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.Unit.CLASS
import com.flatcode.simplemultiapps.Unit.DATA
import com.flatcode.simplemultiapps.Unit.THEME
import com.flatcode.simplemultiapps.Unit.VOID
import com.flatcode.simplemultiapps.databinding.ActivityRandomImgGeneratingBinding
import org.json.JSONArray
import org.json.JSONException

class RandomImgGeneratingActivity : AppCompatActivity() {

    private var binding: ActivityRandomImgGeneratingBinding? = null
    private val context: Context = this@RandomImgGeneratingActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityRandomImgGeneratingBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        binding!!.toolbar.nameSpace.text = getString(R.string.random_img_generating)
        getImage(DATA.API_RANDOM_IMAGE)

        binding!!.refreshBtn.setOnClickListener { getImage(DATA.API_RANDOM_IMAGE) }
    }

    fun getImage(url: String?) {
        // extract the json data
        val queue = Volley.newRequestQueue(this)
        val arrayRequest = JsonArrayRequest(Request.Method.GET, url, null, { response: JSONArray ->
            try {
                val kittyData = response.getJSONObject(0)
                val catUrl = kittyData.getString("url")

                VOID.Glide(context, catUrl, binding!!.kittyImage)
                binding!!.downloadBtn.setOnClickListener {
                    val catUri = Uri.parse(catUrl)
                    val browser = Intent(Intent.ACTION_VIEW, catUri)
                    startActivity(browser)
                }
                binding!!.infoBtn.setOnClickListener {
                    try {
                        val breedsInfo = kittyData.getJSONArray("breeds")
                        if (breedsInfo.isNull(0)) {
                            Toast.makeText(context, "Data Not Found.", Toast.LENGTH_SHORT).show()
                        } else {
                            val breedsData = breedsInfo.getJSONObject(0)
                            val i = Intent(applicationContext, CLASS.IMAGE_INFO)
                            i.putExtra("name", breedsData.getString("name"))
                            i.putExtra("origin", breedsData.getString("origin"))
                            i.putExtra("desc", breedsData.getString("description"))
                            i.putExtra("temp", breedsData.getString("temperament"))
                            i.putExtra("wikiUrl", breedsData.getString("wikipedia_url"))
                            i.putExtra("moreLink", breedsData.getString("vcahospitals_url"))
                            i.putExtra("imageUrl", catUrl)
                            startActivity(i)
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }) { error: VolleyError ->
            Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
        }
        queue.add(arrayRequest)
    }
}