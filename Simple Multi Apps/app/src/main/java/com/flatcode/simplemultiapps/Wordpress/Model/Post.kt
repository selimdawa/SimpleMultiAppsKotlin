package com.flatcode.simplemultiapps.Wordpress.Model

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("id") var id: Int = 0,
    @SerializedName("featured_media") var featured_media: Int = 0,
    @SerializedName("title") var title: JsonObject? = null,
    @SerializedName("excerpt") var excerpt: JsonObject? = null,
    @SerializedName("content") var content: JsonObject? = null,
    var sqLiteId: Int = 0,
    var wpPostId: Int = 0,
    var wpTitle: String? = null,
    var wpExcerpt: String? = null,
    var wpContent: String? = null,
    var isFavorite: Boolean = false
) {

    constructor(
        sqLiteId: Int,
        wpPostId: Int,
        wpTitle: String?,
        wpExcerpt: String?,
        isFavoriteInt: Int
    ) : this(
        id = wpPostId,
        sqLiteId = sqLiteId,
        wpPostId = wpPostId,
        wpTitle = wpTitle,
        wpExcerpt = wpExcerpt,
        isFavorite = isFavoriteInt == 1
    )
}