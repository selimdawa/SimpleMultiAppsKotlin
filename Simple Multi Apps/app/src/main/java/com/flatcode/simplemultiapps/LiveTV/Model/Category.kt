package com.flatcode.simplemultiapps.livetv.model

import java.io.Serializable

data class Category(
    val id: Int = 0,
    val name: String? = null,
    val imageUrl: String? = null
) : Serializable