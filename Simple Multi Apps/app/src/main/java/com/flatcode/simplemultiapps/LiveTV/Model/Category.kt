package com.flatcode.simplemultiapps.LiveTV.Model

import java.io.Serializable

data class Category(
    var id: Int = 0,
    var name: String? = null,
    var imageUrl: String? = null
) : Serializable