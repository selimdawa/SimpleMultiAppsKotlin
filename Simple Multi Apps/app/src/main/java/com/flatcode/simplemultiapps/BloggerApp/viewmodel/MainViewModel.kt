package com.flatcode.simplemultiapps.bloggerapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.littleapp.blogger.model.Post
import com.flatcode.simplemultiapps.bloggerapp.repository.BloggerRepository
import com.littleapp.blogger.utils.DATA

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = BloggerRepository(application)

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> = _posts

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private var nextToken = "1"
    private var allPosts = ArrayList<Post>()

    fun loadPosts(isInitial: Boolean = false) {
        if (isInitial) {
            nextToken = "1"
            allPosts.clear()
        }

        if (nextToken == DATA.END) return

        _loading.value = true
        repository.fetchPosts(nextToken, { posts, next ->
            allPosts.addAll(posts)
            _posts.value = allPosts
            nextToken = next ?: DATA.END
            _loading.value = false
        }, { errorMsg ->
            _error.value = errorMsg
            _loading.value = false
        })
    }

    fun searchPosts(query: String, isInitial: Boolean = false) {
        if (isInitial) {
            nextToken = "1"
            allPosts.clear()
        }

        if (nextToken == DATA.END) return

        _loading.value = true
        repository.searchPosts(query, nextToken, { posts, next ->
            allPosts.addAll(posts)
            _posts.value = allPosts
            nextToken = next ?: DATA.END
            _loading.value = false
        }, { errorMsg ->
            _error.value = errorMsg
            _loading.value = false
        })
    }

    fun getNextToken() = nextToken
}