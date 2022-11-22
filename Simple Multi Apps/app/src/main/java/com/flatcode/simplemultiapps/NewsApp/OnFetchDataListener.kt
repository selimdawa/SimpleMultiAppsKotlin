package com.flatcode.simplemultiapps.NewsApp

import com.flatcode.simplemultiapps.NewsApp.Model.NewsHeadlines

interface OnFetchDataListener<NewsApiResponse> {
    fun onFetchData(list: List<NewsHeadlines?>?, message: String?)
    fun onError(message: String?)
}