package com.flatcode.simplemultiapps.LiveTV.Model

import java.io.Serializable

data class Channel(
    val id: Int = 0,
    val name: String? = null,
    val description: String? = null,
    val thumbnail: String? = null,
    val liveUrl: String? = null,
    val facebook: String? = null,
    val twitter: String? = null,
    val youtube: String? = null,
    val website: String? = null,
    val category: String? = null
) : Serializable