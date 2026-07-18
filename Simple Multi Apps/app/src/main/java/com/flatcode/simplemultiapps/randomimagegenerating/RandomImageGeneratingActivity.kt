package com.flatcode.simplemultiapps.randomimagegenerating

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.utils.DATA
import com.flatcode.simplemultiapps.utils.THEME
import com.flatcode.simplemultiapps.utils.intent1
import com.flatcode.simplemultiapps.utils.loadImage
import com.flatcode.simplemultiapps.databinding.ActivityRandomImageGeneratingBinding
import org.json.JSONException

class RandomImageGeneratingActivity : AppCompatActivity() {

    private var _binding: ActivityRandomImageGeneratingBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(this)
        super.onCreate(savedInstanceState)
        _binding = ActivityRandomImageGeneratingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.nameSpace.text = getString(R.string.random_image_generating)
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

                binding.kittyImage.loadImage(catUrl)

                binding.downloadBtn.setOnClickListener {
                    val browser = Intent(Intent.ACTION_VIEW, Uri.parse(catUrl))
                    startActivity(browser)
                }

                binding.infoBtn.setOnClickListener {
                    try {
                        val breedsInfo = kittyData.getJSONArray(DATA.JSON_BREEDS)
                        if (breedsInfo.isNull(0)) {
                            Toast.makeText(this, R.string.data_not_found, Toast.LENGTH_SHORT).show()
                        } else {
                            val breedsData = breedsInfo.getJSONObject(0)

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
                                if (breedsData.has(DATA.JSON_VCA_HOSPITALS_URL)) breedsData.getString(
                                    DATA.JSON_VCA_HOSPITALS_URL
                                ) else DATA.EMPTY

                            intent1(ImageInfoActivity::class.java) {
                                putExtra(DATA.KEY_NAME, name)
                                putExtra(DATA.KEY_ORIGIN, origin)
                                putExtra(DATA.KEY_DESC, desc)
                                putExtra(DATA.KEY_TEMP, temp)
                                putExtra(DATA.KEY_WIKI_URL, wikiUrl)
                                putExtra(DATA.KEY_MORE_LINK, moreLink)
                                putExtra(DATA.KEY_IMAGE_URL, catUrl)
                            }
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, { error ->
            val message = error.message ?: getString(R.string.unknown_error)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        })
        queue.add(arrayRequest)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}