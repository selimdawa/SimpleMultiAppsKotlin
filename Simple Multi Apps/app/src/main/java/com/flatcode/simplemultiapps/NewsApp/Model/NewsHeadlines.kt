package com.flatcode.simplemultiapps.NewsApp.Model

import com.flatcode.simplemultiapps.Unit.DATA
import java.io.Serializable

data class NewsHeadlines(
    var source: Source? = null,
    var author: String = DATA.EMPTY,
    var title: String = DATA.EMPTY,
    var description: String = DATA.EMPTY,
    var url: String = DATA.EMPTY,
    var urlToImage: String = DATA.EMPTY,
    var publishedAt: String = DATA.EMPTY,
    var content: String = DATA.EMPTY
) : Serializable