package com.flatcode.simplemultiapps.LiveTV.Model

import java.io.Serializable

class Category : Serializable {
    var id = 0
    var name: String? = null
    var image_url: String? = null

    constructor()

    constructor(id: Int, name: String?, image_url: String?) {
        this.id = id
        this.name = name
        this.image_url = image_url
    }
}