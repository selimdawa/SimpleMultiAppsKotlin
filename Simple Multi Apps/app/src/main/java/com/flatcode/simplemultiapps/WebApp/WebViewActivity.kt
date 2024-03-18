package com.flatcode.simplemultiapps.WebApp

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.flatcode.simplemultiapps.Unit.DATA
import com.flatcode.simplemultiapps.Unit.THEME
import com.flatcode.simplemultiapps.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {

    private var binding: ActivityWebViewBinding? = null
    var webName: String? = null
    var context: Context = this@WebViewActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        val data = intent
        webName = data.getStringExtra(DATA.WEB_NAME)

        binding!!.webView.settings.loadsImagesAutomatically = true
        binding!!.webView.settings.javaScriptEnabled = true
        binding!!.webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY

        when (webName) {
            DATA.WEBSITE -> binding!!.webView.loadUrl(DATA.mySite)
            DATA.INSTAGRAM -> binding!!.webView.loadUrl(DATA.myInstagram)
            DATA.FACEBOOK -> binding!!.webView.loadUrl(DATA.myFacebook)
            DATA.TWITTER -> binding!!.webView.loadUrl(DATA.myTwitter)
            else -> binding!!.webView.loadUrl(DATA.mySite)
        }
    }
}