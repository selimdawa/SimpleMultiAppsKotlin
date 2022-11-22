package com.flatcode.simplemultiapps.NewsApp

import com.flatcode.simplemultiapps.NewsApp.Model.NewsHeadlines
import com.flatcode.simplemultiapps.Unit.DATA
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.flatcode.simplemultiapps.NewsApp.OnFetchDataListener
import com.flatcode.simplemultiapps.NewsApp.RequestManger.CallNewsApi
import com.flatcode.simplemultiapps.NewsApp.Model.NewsApiResponse
import android.widget.Toast
import retrofit2.http.GET

interface SelectListener {
    fun onNewsClicked(headlines: NewsHeadlines?)
}