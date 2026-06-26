package com.flatcode.simplemultiapps.LiveTV.Model

import java.io.Serializable

data class Category(
    val id: Int = 0,
    val name: String? = null,
    val imageUrl: String? = null
) : Serializable