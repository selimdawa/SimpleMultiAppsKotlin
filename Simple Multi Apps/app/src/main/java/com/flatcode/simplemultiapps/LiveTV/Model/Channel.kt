package com.flatcode.simplemultiapps.LiveTV.Model

import java.io.Serializable

data class Channel(
    var id: Int = 0,
    var name: String? = null,
    var description: String? = null,
    var thumbnail: String? = null,
    var liveUrl: String? = null,
    var facebook: String? = null,
    var twitter: String? = null,
    var youtube: String? = null,
    var website: String? = null,
    var category: String? = null
) : Serializable