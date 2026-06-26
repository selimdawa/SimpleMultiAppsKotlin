package com.flatcode.simplemultiapps.NewsApp.Model

import com.flatcode.simplemultiapps.Unit.DATA
import java.io.Serializable

data class Source(
    var id: String = DATA.EMPTY,
    var name: String = DATA.EMPTY
) : Serializable