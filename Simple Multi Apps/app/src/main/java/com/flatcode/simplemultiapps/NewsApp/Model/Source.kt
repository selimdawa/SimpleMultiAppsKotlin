package com.flatcode.simplemultiapps.newsapp.model

import com.flatcode.simplemultiapps.utils.DATA
import java.io.Serializable

data class Source(
    var id: String = DATA.EMPTY,
    var name: String = DATA.EMPTY
) : Serializable