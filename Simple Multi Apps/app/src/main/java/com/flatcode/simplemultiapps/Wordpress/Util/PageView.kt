package com.flatcode.simplemultiapps.Wordpress.Util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient

object PageView {

    @SuppressLint("SetJavaScriptEnabled")
    fun initWebView(content: String?, context: Context, webView: WebView, onLoadingStateChanged: ((isLoading: Boolean) -> Unit)? = null) {
        val rawContent = content.orEmpty()

        val formattedHtml = "<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" />" +
                "<script src=\"prism.js\"></script>" +
                "<div class=\"content\">$rawContent</div>"

        with(webView) {
            settings.loadsImagesAutomatically = true
            settings.javaScriptEnabled = true

            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    onLoadingStateChanged?.invoke(true)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    onLoadingStateChanged?.invoke(false)
                }
            }

            loadDataWithBaseURL(
                "file:///android_asset/*",
                formattedHtml,
                "text/html; charset=utf-8",
                "UTF-8",
                null
            )
        }
    }
}