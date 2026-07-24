package com.flatcode.simplemultiapps.webapp

import android.content.Context
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.flatcode.simplemultiapps.utils.DATA
import com.flatcode.simplemultiapps.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {

    private var _binding: ActivityWebViewBinding? = null
    private val binding get() = _binding!!

    private var webName: String? = null
    val context: Context = this@WebViewActivity

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

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