package com.flatcode.simplemultiapps.randomimagegenerating

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.utils.DATA
import com.flatcode.simplemultiapps.utils.loadImage
import com.flatcode.simplemultiapps.databinding.ActivityImageInfoBinding

class ImageInfoActivity : AppCompatActivity() {

    private var _binding: ActivityImageInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        _binding = ActivityImageInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.toolbar.nameSpace.text = getString(R.string.image_info)

        binding.catName.text = intent.getStringExtra(DATA.KEY_NAME).let { if (it.isNullOrEmpty()) DATA.UNKNOWN else it }
        binding.catOrigin.text = intent.getStringExtra(DATA.KEY_ORIGIN).let { if (it.isNullOrEmpty()) DATA.UNKNOWN else it }
        binding.catDescription.text = intent.getStringExtra(DATA.KEY_DESC).let { if (it.isNullOrEmpty()) DATA.UNKNOWN else it }
        binding.catTemperament.text = intent.getStringExtra(DATA.KEY_TEMP).let { if (it.isNullOrEmpty()) DATA.UNKNOWN else it }

        binding.catImage.loadImage(intent.getStringExtra(DATA.KEY_IMAGE_URL))

        binding.wikiBtn.setOnClickListener {
            val wikiUrl = intent.getStringExtra(DATA.KEY_WIKI_URL)
            if (!wikiUrl.isNullOrEmpty()) {
                val browser = Intent(Intent.ACTION_VIEW, Uri.parse(wikiUrl))
                startActivity(browser)
            } else {
                Toast.makeText(this, "Wikipedia link not available", Toast.LENGTH_SHORT).show()
            }
        }

        binding.moreInfoBtn.setOnClickListener {
            val moreLink = intent.getStringExtra(DATA.KEY_MORE_LINK)
            if (!moreLink.isNullOrEmpty()) {
                val browser = Intent(Intent.ACTION_VIEW, Uri.parse(moreLink))
                startActivity(browser)
            } else {
                Toast.makeText(this, "More info link not available", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}