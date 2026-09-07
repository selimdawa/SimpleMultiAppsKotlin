package com.flatcode.simplemultiapps.utils

@Suppress("SpellCheckingInspection")
object DATA {
    //Database
    const val EMPTY = ""
    const val DATA = "data"
    const val END = "end"
    var searchStatus = false

    //Intent Keys
    const val ID = "id"
    const val PAGE_ID = "pageId"
    const val POST_ID = "postId"
    const val FEATURED_MEDIA = "featuredMedia"
    const val POST_TITLE = "postTitle"
    const val POST_CONTENT = "postContent"
    const val POST_EXCERPT = "postExcerpt"
    const val CATEGORY_NAME = "categoryName"
    const val CATEGORY = "category"
    const val JOKES_URL = "extra_jokes_url"
    const val ANY = "Any"
    const val AMOUNT_PARAM = "amount=10"
    const val URI = "uri"
    const val PAGE_NUMBER = "pageNumber"
    const val PDF_PASSWORD = "pdfPassword"

    //JSON Keys
    const val ITEMS = "items"
    const val NEXT_PAGE_TOKEN = "nextPageToken"
    const val AUTHOR = "author"
    const val IMAGE = "image"
    const val IMG = "img"
    const val SRC = "src"
    const val THUMBNAIL = "thumbnail"
    const val LIVE_URL = "live_url"
    const val FACEBOOK_KEY = "facebook"
    const val TWITTER_KEY = "twitter"
    const val YOUTUBE = "youtube"
    const val WEBSITE_KEY = "website"
    const val POSTS = "posts"
    const val PAGES = "pages"
    const val COMMENTS = "comments"
    const val NAME = "name"
    const val DESCRIPTION = "description"
    const val IMAGE_URL_KEY = "image_url"
    const val JOKES = "jokes"
    const val AMOUNT = "amount"
    const val TYPE = "type"
    const val JOKE = "joke"
    const val SETUP = "setup"
    const val DELIVERY = "delivery"
    const val TITLE = "title"
    const val CONTENT = "content"
    const val PUBLISHED = "published"
    const val UPDATED = "updated"
    const val URL = "url"
    const val SELF_LINK = "selfLink"
    const val DISPLAY_NAME = "displayName"

    //Preferences
    const val FIRST_INSTALL = "firstInstall"
    const val SCREEN_ON_PREF = "screen_on_pref"
    const val PDF_THEME_PREF = "pdf_theme_pref"
    const val QUALITY_PREF = "quality_pref"
    const val ALIAS_PREF = "alias_pref"
    const val SCROLL_PREF = "scroll_pref"
    const val SNAP_PREF = "snap_pref"
    const val FLING_PREF = "fling_pref"

    //Lists
    val VALUES = listOf(
        "One",
        "Two",
        "Three",
        "Five",
        "Sex",
        "Seven",
        "Eight",
        "Nine",
        "Ten",
        "Eleven",
        "Twelve",
        "Thirteen",
        "Fourteen",
        "Fifteen"
    )

    //Jokes
    val JOKE_CATEGORIES = listOf("Any", "Programming", "Dark", "Spooky", "Misc", "Pun", "Christmas")
    const val JOKE_TYPE_SINGLE = "single"

    //Extensions & Schemes
    const val TEXT_PLAIN = "text/plain"
    const val APPLICATION_PDF = "application/pdf"
    const val HTTP = "http"
    const val SCHEME_CONTENT = "content"
    const val MAILTO_SCHEME = "mailto:"
    const val TEL_SCHEME = "tel:"
    const val MARKET_SCHEME = "market://details?id="
    const val PERMISSION_DENIED = "Permission denied"
    const val META_DIALOG = "meta_dialog"
    const val ERROR_HTTP_REQUEST = "Error during http request, response code : "
    const val ERROR_SSL = "SSL Error cannot get file at URL : "
    const val ERROR_IO = "Error cannot get file at URL : "
    const val TIME_FORMAT = "%02d:%02d:%02d"
    const val INPUT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
    const val OUTPUT_DATE_FORMAT = "dd/MM/yyyy K:mm a"
    const val TEXT_HTML = "text/html"
    const val UTF_8 = "UTF-8"
    const val LABELS = "labels"
    const val REFRESH_DELAY = 3000L
    const val INS_START = "<ins"
    const val INS_END = "</ins>"
    const val IFRAME_START = "<iframe"
    const val IFRAME_END = "/iframe>"
    const val VIDEO_WRAPPER_START = "<div class=\"videoWrapper\">"
    const val VIDEO_WRAPPER_END = "</div>"

    //Live TV Categories
    const val SLIDER = "slider"
    const val DETAILS = "details"
    const val NEWS = "News"
    const val SPORTS = "Sports"
    const val ENTERTAINMENT = "Entertainment"
    const val LIVETV_API_KEY = "1A4mgi2rBHCJdqggsYVx"

    //Blogger
    const val BLOGGER_API = "AIzaSyDAq5n9ShBngyuSoWrFBnuena94qPm2Gk0" // API your blogger
    const val BLOG_ID = "5758825298436553050" // ID for your blogger
    const val MAX_POST_RESULTS = "10" // Max post display

    //Web App
    var myFacebook = "https://www.facebook.com" // FB here
    var myTwitter = "https://www.twitter.com" // Twitter here
    var myInstagram = "https://www.instagram.com" // Instagram here
    var mySite = "https://www.google.com" // WebSite here
    var myMobileNumber = "+963994683386" // Mobile Number here
    var myEmail = "selimdawa@gmail.com" // Email here
    var WEB_NAME = "webName"
    var WEBSITE = "website"
    var INSTAGRAM = "instagram"
    var FACEBOOK = "facebook"
    var TWITTER = "twitter"

    //API & Urls
    const val BLOGGER_BASE_URL = "https://www.googleapis.com/blogger/v3/blogs/"
    const val JOKE_BASE_URL = "https://v2.jokeapi.dev/joke/"
    const val NEWS_BASE_URL = "https://newsapi.org/v2/"
    const val NEWS_API_KEY = "07f40de92d3644908496e8f9677ee838"
    const val GENERAL = "general"
    const val COUNTRY_US = "us"
    const val TOP_HEADLINES = "top-headlines"
    var API_RANDOM_IMAGE = "https://api.thecatapi.com/v1/images/search?has_breeds=1"
    var IP_LIVE_TV = "192.168.1.2" // IP My Computer

    //Random Img Generating
    const val KEY_NAME = "name"
    const val KEY_ORIGIN = "origin"
    const val KEY_DESC = "desc"
    const val KEY_TEMP = "temp"
    const val KEY_WIKI_URL = "wikiUrl"
    const val KEY_MORE_LINK = "moreLink"
    const val KEY_IMAGE_URL = "imageUrl"
    const val JSON_URL = "url"
    const val JSON_BREEDS = "breeds"
    const val JSON_NAME = "name"
    const val JSON_ORIGIN = "origin"
    const val JSON_DESCRIPTION = "description"
    const val JSON_TEMPERAMENT = "temperament"
    const val JSON_WIKIPEDIA_URL = "wikipedia_url"
    const val JSON_VCA_HOSPITALS_URL = "vcahospitals_url"
}