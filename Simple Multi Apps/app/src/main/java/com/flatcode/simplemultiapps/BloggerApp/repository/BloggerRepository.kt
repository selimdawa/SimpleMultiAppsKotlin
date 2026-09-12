package com.flatcode.simplemultiapps.bloggerapp.repository

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.flatcode.simplemultiapps.bloggerapp.model.Comment
import com.flatcode.simplemultiapps.bloggerapp.model.Page
import com.flatcode.simplemultiapps.bloggerapp.model.Post
import com.flatcode.simplemultiapps.utils.DATA
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.parser.Parser

class BloggerRepository(private val context: Context) {

    private val requestQueue = Volley.newRequestQueue(context.applicationContext)

    fun fetchPosts(nextToken: String, callback: (List<Post>, String?) -> Unit, errorCallback: (String) -> Unit) {
        val url = if (nextToken == "1" || nextToken == DATA.EMPTY) {
            "${DATA.FEED_URL}?start-index=1&max-results=${DATA.MAX_POST_RESULTS}"
        } else {
            "${DATA.FEED_URL}?start-index=$nextToken&max-results=${DATA.MAX_POST_RESULTS}"
        }

        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            val posts = ArrayList<Post>()
            try {
                val doc = Jsoup.parse(response ?: DATA.EMPTY, "", Parser.xmlParser())
                val entries = doc.select("entry")

                for (entry in entries) {
                    val post = parsePost(entry)
                    posts.add(post)
                }

                val newNextToken = if (entries.size < DATA.MAX_POST_RESULTS.toInt()) {
                    DATA.END
                } else {
                    val currentStart = if (nextToken == "1" || nextToken == DATA.EMPTY) 1 else nextToken.toInt()
                    (currentStart + entries.size).toString()
                }
                callback(posts, newNextToken)
            } catch (e: Exception) {
                errorCallback(e.message ?: "Error parsing posts")
            }
        }) { error ->
            errorCallback(error.message ?: "Network error")
        }
        requestQueue.add(stringRequest)
    }

    fun searchPosts(query: String, nextToken: String, callback: (List<Post>, String?) -> Unit, errorCallback: (String) -> Unit) {
        val url = "${DATA.FEED_URL}?q=$query&start-index=$nextToken&max-results=${DATA.MAX_POST_RESULTS}"

        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            val posts = ArrayList<Post>()
            try {
                val doc = Jsoup.parse(response ?: DATA.EMPTY, "", Parser.xmlParser())
                val entries = doc.select("entry")

                for (entry in entries) {
                    val post = parsePost(entry)
                    posts.add(post)
                }

                val newNextToken = if (entries.size < DATA.MAX_POST_RESULTS.toInt()) {
                    DATA.END
                } else {
                    (nextToken.toInt() + entries.size).toString()
                }
                callback(posts, newNextToken)
            } catch (e: Exception) {
                errorCallback(e.message ?: "Error parsing search results")
            }
        }) { error ->
            errorCallback(error.message ?: "Network error")
        }
        requestQueue.add(stringRequest)
    }

    fun fetchPages(callback: (List<Page>) -> Unit, errorCallback: (String) -> Unit) {
        val url = DATA.PAGES_FEED_URL
        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            val pages = ArrayList<Page>()
            try {
                val doc = Jsoup.parse(response ?: DATA.EMPTY, "", Parser.xmlParser())
                val entries = doc.select("entry")
                for (entry in entries) {
                    pages.add(parsePage(entry))
                }
                callback(pages)
            } catch (e: Exception) {
                errorCallback(e.message ?: "Error parsing pages")
            }
        }) { error ->
            errorCallback(error.message ?: "Network error")
        }
        requestQueue.add(stringRequest)
    }

    fun fetchPostDetails(postId: String, callback: (Post) -> Unit, errorCallback: (String) -> Unit) {
        val url = "https://www.blogger.com/feeds/${DATA.BLOG_ID}/posts/default/$postId"
        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            try {
                val doc = Jsoup.parse(response ?: DATA.EMPTY, "", Parser.xmlParser())
                val entry = doc.selectFirst("entry")
                if (entry != null) {
                    callback(parsePost(entry))
                } else {
                    errorCallback("Post not found")
                }
            } catch (e: Exception) {
                errorCallback(e.message ?: "Error parsing post details")
            }
        }) { error ->
            errorCallback(error.message ?: "Network error")
        }
        requestQueue.add(stringRequest)
    }

    fun fetchPageDetails(pageId: String, callback: (Page) -> Unit, errorCallback: (String) -> Unit) {
        val url = "https://www.blogger.com/feeds/${DATA.BLOG_ID}/pages/default/$pageId"
        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            try {
                val doc = Jsoup.parse(response ?: DATA.EMPTY, "", Parser.xmlParser())
                val entry = doc.selectFirst("entry")
                if (entry != null) {
                    callback(parsePage(entry))
                } else {
                    errorCallback("Page not found")
                }
            } catch (e: Exception) {
                errorCallback(e.message ?: "Error parsing page details")
            }
        }) { error ->
            errorCallback(error.message ?: "Network error")
        }
        requestQueue.add(stringRequest)
    }

    fun fetchComments(postId: String, callback: (List<Comment>) -> Unit, errorCallback: (String) -> Unit) {
        val url = "https://www.blogger.com/feeds/${DATA.BLOG_ID}/$postId/comments/default"
        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            val comments = ArrayList<Comment>()
            try {
                val doc = Jsoup.parse(response ?: DATA.EMPTY, "", Parser.xmlParser())
                val entries = doc.select("entry")
                for (entry in entries) {
                    val id = entry.selectFirst("id")?.text()?.split("-")?.last() ?: ""
                    val published = entry.selectFirst("published")?.text() ?: ""
                    val content = entry.selectFirst("content")?.text() ?: ""
                    val displayName = entry.select("author name").first()?.text() ?: DATA.UNKNOWN
                    val profileImage = entry.select("author gd|image").attr("src").ifEmpty {
                        entry.select("author link[rel=image]").attr("href").ifEmpty {
                            "https://www.blogger.com/img/blogger-logotype-color-black-caps.png"
                        }
                    }
                    comments.add(Comment(id, displayName, profileImage, published, content))
                }
                callback(comments)
            } catch (e: Exception) {
                errorCallback(e.message ?: "Error parsing comments")
            }
        }) { error ->
            errorCallback(error.message ?: "Network error")
        }
        requestQueue.add(stringRequest)
    }

    private fun parsePost(entry: Element): Post {
        val id = entry.selectFirst("id")?.text()?.split("-")?.last() ?: ""
        val title = entry.selectFirst("title")?.text() ?: ""
        val content = entry.selectFirst("content")?.text() ?: ""
        val published = entry.selectFirst("published")?.text() ?: ""
        val updated = entry.selectFirst("updated")?.text() ?: ""
        val urlPath = entry.selectFirst("link[rel=alternate]")?.attr("href") ?: ""
        val selfLink = entry.selectFirst("link[rel=self]")?.attr("href") ?: ""
        val authorName = entry.select("author name").first()?.text() ?: DATA.UNKNOWN
        
        val labels = entry.select("category").mapNotNull { it.attr("term").takeIf { term -> term.isNotEmpty() } }
        
        return Post(authorName, content, id, published, selfLink, title, updated, urlPath, labels)
    }

    private fun parsePage(entry: Element): Page {
        val id = entry.selectFirst("id")?.text()?.split("-")?.last() ?: ""
        val title = entry.selectFirst("title")?.text() ?: ""
        val content = entry.selectFirst("content")?.text() ?: ""
        val published = entry.selectFirst("published")?.text() ?: ""
        val updated = entry.selectFirst("updated")?.text() ?: ""
        val urlPath = entry.selectFirst("link[rel=alternate]")?.attr("href") ?: ""
        val selfLink = entry.selectFirst("link[rel=self]")?.attr("href") ?: ""
        val authorName = entry.select("author name").first()?.text() ?: DATA.UNKNOWN
        return Page(authorName, content, id, published, selfLink, title, updated, urlPath)
    }
}