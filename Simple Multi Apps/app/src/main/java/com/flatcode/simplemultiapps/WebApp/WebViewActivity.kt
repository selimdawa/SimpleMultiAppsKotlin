package com.flatcode.simplemultiapps.webapp

import android.content.Context
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.flatcode.simplemultiapps.utils.DATA
import com.flatcode.simplemultiapps.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {

    private var _binding: ActivityWebViewBinding? = null
    private val binding get() = _binding!!

    private var webName: String? = null
    val context: Context = this@WebViewActivity

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        _binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        webName = intent.getStringExtra(DATA.WEB_NAME)

        with(binding.webView) {
            settings.loadsImagesAutomatically = true
            settings.javaScriptEnabled = true
            scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY

            val url = when (webName) {
                DATA.WEBSITE -> DATA.mySite
                DATA.INSTAGRAM -> DATA.myInstagram
                DATA.FACEBOOK -> DATA.myFacebook
                DATA.TWITTER -> DATA.myTwitter
                else -> DATA.mySite
            }
            loadUrl(url)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}