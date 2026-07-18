package com.flatcode.simplemultiapps.newsapp

import com.flatcode.simplemultiapps.newsapp.model.NewsHeadlines

interface SelectListener {
    fun onNewsClicked(headlines: NewsHeadlines?)
}