package com.flatcode.simplemultiapps.newsapp.activity

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.BundleCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.flatcode.simplemultiapps.newsapp.model.NewsHeadlines
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.utils.DATA
import com.flatcode.simplemultiapps.utils.loadImage
import com.flatcode.simplemultiapps.databinding.ActivityNewsAppDetailsBinding

class NewsAppDetailsActivity : AppCompatActivity() {

    private var _binding: ActivityNewsAppDetailsBinding? = null
    private val binding get() = _binding!!

    private var headlines: NewsHeadlines? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        _binding = ActivityNewsAppDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(enabled = true) {
                override fun handleOnBackPressed() {
                    finish()
                }
            },
        )

        intent.extras?.let { bundle ->
            headlines = BundleCompat.getSerializable(bundle, DATA.DATA, NewsHeadlines::class.java)
        }

        binding.nameSpace.setText(R.string.post_details)
        binding.back.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        headlines?.let { data ->
            with(binding) {
                title.text = data.title
                author.text = data.author
                time.text = data.publishedAt
                detail.text = data.description
                content.text = data.content
            }

            binding.image.loadImage(data.urlToImage)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}