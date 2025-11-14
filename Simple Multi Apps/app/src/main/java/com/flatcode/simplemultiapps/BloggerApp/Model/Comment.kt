package com.flatcode.simplemultiapps.BloggerApp.Model

class Comment {
    var id: String? = null
    var name: String? = null
    var profileImage: String? = null
    var published: String? = null
    var comment: String? = null

    constructor()

    constructor(
        id: String?, name: String?, profileImage: String?, published: String?, comment: String?,
    ) {
        this.id = id
        this.name = name
        this.profileImage = profileImage
        this.published = published
        this.comment = comment
    }
}