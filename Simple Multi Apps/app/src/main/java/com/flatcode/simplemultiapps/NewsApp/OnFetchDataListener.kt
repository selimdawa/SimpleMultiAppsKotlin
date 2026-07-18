package com.flatcode.simplemultiapps.newsapp

import com.flatcode.simplemultiapps.newsapp.model.NewsHeadlines

interface OnFetchDataListener<NewsApiResponse> {
    fun onFetchData(list: List<NewsHeadlines?>?, message: String?)
    fun onError(message: String?)
}