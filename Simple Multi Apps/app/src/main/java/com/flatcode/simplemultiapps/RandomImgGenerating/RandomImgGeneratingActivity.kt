package com.flatcode.simplemultiapps.RandomImgGenerating

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.Unit.CLASS
import com.flatcode.simplemultiapps.Unit.DATA
import com.flatcode.simplemultiapps.Unit.THEME
import com.flatcode.simplemultiapps.Unit.VOID
import com.flatcode.simplemultiapps.databinding.ActivityRandomImgGeneratingBinding
import org.json.JSONException

class RandomImgGeneratingActivity : AppCompatActivity() {

    private var _binding: ActivityRandomImgGeneratingBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(this)
        super.onCreate(savedInstanceState)
        _binding = ActivityRandomImgGeneratingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.nameSpace.text = getString(R.string.random_img_generating)
        getImage(DATA.API_RANDOM_IMAGE)

        binding.refreshBtn.setOnClickListener { getImage(DATA.API_RANDOM_IMAGE) }
    }

    fun getImage(url: String?) {
        if (url == null) return

        val queue = Volley.newRequestQueue(this)
        val arrayRequest = JsonArrayRequest(Request.Method.GET, url, null, { response ->
            try {
                val kittyData = response.getJSONObject(0)
                val catUrl = kittyData.getString(DATA.JSON_URL)

                VOID.Glide(this, catUrl, binding.kittyImage)

                binding.downloadBtn.setOnClickListener {
                    val browser = Intent(Intent.ACTION_VIEW, Uri.parse(catUrl))
                    startActivity(browser)
                }

                binding.infoBtn.setOnClickListener {
                    try {
                        val breedsInfo = kittyData.getJSONArray(DATA.JSON_BREEDS)
                        if (breedsInfo.isNull(0)) {
                            Toast.makeText(this, "Data Not Found.", Toast.LENGTH_SHORT).show()
                        } else {
                            val breedsData = breedsInfo.getJSONObject(0)
                            val i = Intent(this, CLASS.IMAGE_INFO)

                            val name =
                                if (breedsData.has(DATA.JSON_NAME)) breedsData.getString(DATA.JSON_NAME) else DATA.EMPTY
                            val origin =
                                if (breedsData.has(DATA.JSON_ORIGIN)) breedsData.getString(DATA.JSON_ORIGIN) else DATA.EMPTY
                            val desc =
                                if (breedsData.has(DATA.JSON_DESCRIPTION)) breedsData.getString(DATA.JSON_DESCRIPTION) else DATA.EMPTY
                            val temp =
                                if (breedsData.has(DATA.JSON_TEMPERAMENT)) breedsData.getString(DATA.JSON_TEMPERAMENT) else DATA.EMPTY
                            val wikiUrl =
                                if (breedsData.has(DATA.JSON_WIKIPEDIA_URL)) breedsData.getString(
                                    DATA.JSON_WIKIPEDIA_URL
                                ) else DATA.EMPTY
                            val moreLink =
                                if (breedsData.has(DATA.JSON_VCAHOSPITALS_URL)) breedsData.getString(
                                    DATA.JSON_VCAHOSPITALS_URL
                                ) else DATA.EMPTY

                            i.putExtra(DATA.KEY_NAME, name)
                            i.putExtra(DATA.KEY_ORIGIN, origin)
                            i.putExtra(DATA.KEY_DESC, desc)
                            i.putExtra(DATA.KEY_TEMP, temp)
                            i.putExtra(DATA.KEY_WIKI_URL, wikiUrl)
                            i.putExtra(DATA.KEY_MORE_LINK, moreLink)
                            i.putExtra(DATA.KEY_IMAGE_URL, catUrl)
                            startActivity(i)
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, { error ->
            val message = error.message ?: "An unknown error occurred"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        })
        queue.add(arrayRequest)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}