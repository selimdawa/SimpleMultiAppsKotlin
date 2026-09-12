package com.flatcode.simplemultiapps.bloggerapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.flatcode.simplemultiapps.bloggerapp.model.Comment
import com.flatcode.simplemultiapps.bloggerapp.model.Post
import com.flatcode.simplemultiapps.bloggerapp.repository.BloggerRepository

class PostDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = BloggerRepository(application)

    private val _post = MutableLiveData<Post>()
    val post: LiveData<Post> = _post

    private val _comments = MutableLiveData<List<Comment>>()
    val comments: LiveData<List<Comment>> = _comments

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadPostDetails(postId: String) {
        _loading.value = true
        repository.fetchPostDetails(postId, { postData ->
            _post.value = postData
            _loading.value = false
        }, { errorMsg ->
            _error.value = errorMsg
            _loading.value = false
        })
    }

    fun loadComments(postId: String) {
        repository.fetchComments(postId, { commentsList ->
            _comments.value = commentsList
        }, { errorMsg ->
            _error.value = errorMsg
        })
    }
}