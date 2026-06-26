package com.flatcode.simplemultiapps.NewsApp.Model

import java.io.Serializable

data class NewsApiResponse(
    var status: String? = null,
    var totalResults: Int = 0,
    var articles: List<NewsHeadlines>? = null
) : Serializable