package com.flatcode.simplemultiapps.LiveTV.Model

import java.io.Serializable

class Channel : Serializable {
    var id = 0
    var name: String? = null
    var description: String? = null
    var thumbnail: String? = null
    var live_url: String? = null
    var facebook: String? = null
    var twitter: String? = null
    var youtube: String? = null
    var website: String? = null
    var category: String? = null

    override fun toString(): String {
        return "Channel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", live_url='" + live_url + '\'' +
                ", facebook='" + facebook + '\'' +
                ", twitter='" + twitter + '\'' +
                ", youtube='" + youtube + '\'' +
                ", website='" + website + '\'' +
                ", category='" + category + '\'' +
                '}'
    }
}