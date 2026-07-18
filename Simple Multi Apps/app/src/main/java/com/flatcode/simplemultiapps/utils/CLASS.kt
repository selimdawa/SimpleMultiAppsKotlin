package com.flatcode.simplemultiapps.utils

import com.flatcode.simplemultiapps.bloggerapp.activity.BloggerAppActivity
import com.flatcode.simplemultiapps.bloggerapp.activity.PageDetailsActivity
import com.flatcode.simplemultiapps.bloggerapp.activity.PagesActivity
import com.flatcode.simplemultiapps.bloggerapp.activity.PostDetailsActivity
import com.flatcode.simplemultiapps.candycrushgame.CandyCrushGameActivity
import com.flatcode.simplemultiapps.jokeapp.activity.JokeAppActivity
import com.flatcode.simplemultiapps.livetv.activity.CategoriesActivity
import com.flatcode.simplemultiapps.livetv.activity.CategoryDetailsActivity
import com.flatcode.simplemultiapps.livetv.activity.LiveTVActivity
import com.flatcode.simplemultiapps.livetv.activity.LiveTVDetailsActivity
import com.flatcode.simplemultiapps.mainapp.MainActivity
import com.flatcode.simplemultiapps.mainapp.SplashActivity
import com.flatcode.simplemultiapps.multipledelete.activity.MultiDeleteActivity
import com.flatcode.simplemultiapps.newsapp.activity.NewsAppActivity
import com.flatcode.simplemultiapps.newsapp.activity.NewsAppDetailsActivity
import com.flatcode.simplemultiapps.pdfreader.activity.PdfReaderActivity
import com.flatcode.simplemultiapps.pdfreader.activity.PdfReaderIntroActivity
import com.flatcode.simplemultiapps.randomimggenerating.ImageInfoActivity
import com.flatcode.simplemultiapps.randomimggenerating.RandomImgGeneratingActivity
import com.flatcode.simplemultiapps.stopwatch.StopWatchActivity
import com.flatcode.simplemultiapps.videoplayer.activity.PlayerActivity
import com.flatcode.simplemultiapps.videoplayer.activity.VideoFolderActivity
import com.flatcode.simplemultiapps.videoplayer.activity.VideoPlayerActivity
import com.flatcode.simplemultiapps.webapp.WebAppActivity
import com.flatcode.simplemultiapps.webapp.WebViewActivity
import com.flatcode.simplemultiapps.wordpress.activity.WordpressActivity
import com.flatcode.simplemultiapps.wordpress.activity.WordpressDetailsActivity
import com.flatcode.simplemultiapps.wordpress.activity.WordpressFavoritesActivity

object CLASS {
    var MAIN: Class<*> = MainActivity::class.java
    var SPLASH: Class<*> = SplashActivity::class.java
    var STOP_WATCH: Class<*> = StopWatchActivity::class.java
    var RANDOM_IMG_GENERATING: Class<*> = RandomImgGeneratingActivity::class.java
    var IMAGE_INFO: Class<*> = ImageInfoActivity::class.java
    var BLOGGER_APP: Class<*> = BloggerAppActivity::class.java
    var BLOGGER_PAGES: Class<*> = PagesActivity::class.java
    var BLOGGER_PAGES_DETAILS: Class<*> = PageDetailsActivity::class.java
    var BLOGGER_POST_DETAILS: Class<*> = PostDetailsActivity::class.java
    var CANDY_CRUSH_GAME: Class<*> = CandyCrushGameActivity::class.java
    var JOKE_APP: Class<*> = JokeAppActivity::class.java
    var MULTIPLE_DELETE: Class<*> = MultiDeleteActivity::class.java
    var WEB_APP: Class<*> = WebAppActivity::class.java
    var WEB_VIEW: Class<*> = WebViewActivity::class.java
    var LIVE_TV: Class<*> = LiveTVActivity::class.java
    var LIVE_TV_CATEGORIES: Class<*> = CategoriesActivity::class.java
    var LIVE_TV_DETAILS: Class<*> = LiveTVDetailsActivity::class.java
    var LIVE_TV_CATEGORIES_DETAILS: Class<*> = CategoryDetailsActivity::class.java
    var VIDEO_PLAYER: Class<*> = VideoPlayerActivity::class.java
    var VIDEO_FOLDER: Class<*> = VideoFolderActivity::class.java
    var VIDEO_PLAY: Class<*> = PlayerActivity::class.java
    var PDF_READER: Class<*> = PdfReaderActivity::class.java
    var PDF_READER_INTRO: Class<*> = PdfReaderIntroActivity::class.java
    var NEWS_APP: Class<*> = NewsAppActivity::class.java
    var NEWS_APP_DETAILS: Class<*> = NewsAppDetailsActivity::class.java
    var WORDPRESS: Class<*> = WordpressActivity::class.java
    var WORDPRESS_DETAILS: Class<*> = WordpressDetailsActivity::class.java
    var WORDPRESS_FAVORITES: Class<*> = WordpressFavoritesActivity::class.java
}