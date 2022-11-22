package com.flatcode.simplemultiapps.Unit

import com.flatcode.simplemultiapps.BloggerApp.Activity.BloggerAppActivity
import com.flatcode.simplemultiapps.BloggerApp.Activity.PageDetailsActivity
import com.flatcode.simplemultiapps.BloggerApp.Activity.PagesActivity
import com.flatcode.simplemultiapps.BloggerApp.Activity.PostDetailsActivity
import com.flatcode.simplemultiapps.CandyCrushGame.CandyCrushGameActivity
import com.flatcode.simplemultiapps.JokeApp.JokeAppActivity
import com.flatcode.simplemultiapps.LiveTV.Activity.CategoriesActivity
import com.flatcode.simplemultiapps.LiveTV.Activity.CategoryDetailsActivity
import com.flatcode.simplemultiapps.LiveTV.Activity.LiveTVActivity
import com.flatcode.simplemultiapps.LiveTV.Activity.LiveTVDetailsActivity
import com.flatcode.simplemultiapps.MainApp.MainActivity
import com.flatcode.simplemultiapps.MainApp.SplashActivity
import com.flatcode.simplemultiapps.MultipleDelete.MultiDeleteActivity
import com.flatcode.simplemultiapps.NewsApp.Activity.NewsAppActivity
import com.flatcode.simplemultiapps.NewsApp.Activity.NewsAppDetailsActivity
import com.flatcode.simplemultiapps.PdfReader.Activity.PdfReaderActivity
import com.flatcode.simplemultiapps.PdfReader.Activity.PdfReaderIntroActivity
import com.flatcode.simplemultiapps.RandomImgGenerating.ImageInfoActivity
import com.flatcode.simplemultiapps.RandomImgGenerating.RandomImgGeneratingActivity
import com.flatcode.simplemultiapps.StopWatch.StopWatchActivity
import com.flatcode.simplemultiapps.VideoPlayer.Activity.PlayerActivity
import com.flatcode.simplemultiapps.VideoPlayer.Activity.VideoFolderActivity
import com.flatcode.simplemultiapps.VideoPlayer.Activity.VideoPlayerActivity
import com.flatcode.simplemultiapps.WebApp.WebAppActivity
import com.flatcode.simplemultiapps.WebApp.WebViewActivity
import com.flatcode.simplemultiapps.Wordpress.Activity.WordpressActivity
import com.flatcode.simplemultiapps.Wordpress.Activity.WordpressDetailsActivity
import com.flatcode.simplemultiapps.Wordpress.Activity.WordpressFavoritesActivity

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