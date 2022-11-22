package com.flatcode.simplemultiapps.Wordpress.Model

import com.google.gson.annotations.SerializedName
import com.google.gson.JsonObject
import android.provider.BaseColumns
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.flatcode.simplemultiapps.Unit.THEME
import com.flatcode.simplemultiapps.R
import android.app.ProgressDialog
import com.google.android.material.snackbar.Snackbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import android.annotation.SuppressLint
import android.webkit.WebViewClient
import android.webkit.WebView
import android.webkit.WebResourceRequest
import com.google.android.material.appbar.CollapsingToolbarLayout
import android.graphics.Bitmap
import android.content.Intent
import com.flatcode.simplemultiapps.Wordpress.Activity.WordpressDetailsActivity
import android.net.ConnectivityManager
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.annotation.RequiresApi
import android.os.Build
import android.widget.TextView
import android.text.Html
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.flatcode.simplemultiapps.Unit.VOID
import com.flatcode.simplemultiapps.Unit.CLASS
import retrofit2.http.GET

class Post {
    //Setter
    // Getter
    @SerializedName("id")
    var id = 0

    @SerializedName("featured_media")
    var featured_media = 0

    @SerializedName("title")
    var title: JsonObject? = null

    @SerializedName("excerpt")
    var excerpt: JsonObject? = null

    @SerializedName("content")
    var content: JsonObject? = null

    //SetterSQLite
    //Getter SQLite
    //Variable for SQLite
    var sqLiteId = 0
    var wpPostId = 0
    var wpTitle: String? = null
    var wpExcerpt: String? = null
    var wpContent: String? = null
    var isFavorite = false

    constructor()
    constructor(
        sqLiteId: Int,
        wpPostId: Int,
        wpTitle: String?,
        wpExcerpt: String?,
        isFavorite: Int
    ) {
        this.sqLiteId = sqLiteId
        this.wpPostId = wpPostId
        this.wpTitle = wpTitle
        this.wpExcerpt = wpExcerpt
        wpContent = wpContent
        this.isFavorite = false
        if (isFavorite == 1) {
            this.isFavorite = true
        }
    }
}