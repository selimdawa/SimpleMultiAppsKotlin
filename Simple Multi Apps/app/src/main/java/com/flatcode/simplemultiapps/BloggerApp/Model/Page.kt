package com.flatcode.simplemultiapps.BloggerApp.Model

class Page {
    var authorName: String? = null
    var content: String? = null
    var id: String? = null
    var published: String? = null
    var selfLink: String? = null
    var title: String? = null
    var updated: String? = null
    var url: String? = null

    constructor()

    constructor(
        authorName: String?, content: String?, id: String?, published: String?,
        selfLink: String?, title: String?, updated: String?, url: String?
    ) {
        this.authorName = authorName
        this.content = content
        this.id = id
        this.published = published
        this.selfLink = selfLink
        this.title = title
        this.updated = updated
        this.url = url
    }
}