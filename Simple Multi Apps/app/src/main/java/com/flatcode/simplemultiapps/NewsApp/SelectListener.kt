package com.flatcode.simplemultiapps.NewsApp

import com.flatcode.simplemultiapps.NewsApp.Model.NewsHeadlines

interface SelectListener {
    fun onNewsClicked(headlines: NewsHeadlines?)
}