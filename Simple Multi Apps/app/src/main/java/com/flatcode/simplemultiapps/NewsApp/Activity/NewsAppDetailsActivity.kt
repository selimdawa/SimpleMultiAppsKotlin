package com.flatcode.simplemultiapps.NewsApp.Activity

import android.content.Context
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.BundleCompat
import com.flatcode.simplemultiapps.NewsApp.Model.NewsHeadlines
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.Unit.DATA
import com.flatcode.simplemultiapps.Unit.THEME
import com.flatcode.simplemultiapps.Unit.VOID
import com.flatcode.simplemultiapps.databinding.ActivityNewsAppDetailsBinding

class NewsAppDetailsActivity : AppCompatActivity() {

    private var _binding: ActivityNewsAppDetailsBinding? = null
    private val binding get() = _binding!!

    private var headlines: NewsHeadlines? = null
    private val context: Context = this@NewsAppDetailsActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        _binding = ActivityNewsAppDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })

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

            VOID.Glide(context, data.urlToImage, binding.image)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}