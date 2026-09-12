package com.flatcode.simplemultiapps.bloggerapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.flatcode.simplemultiapps.bloggerapp.model.Page
import com.flatcode.simplemultiapps.bloggerapp.repository.BloggerRepository

class PageDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = BloggerRepository(application)

    private val _page = MutableLiveData<Page>()
    val page: LiveData<Page> = _page

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadPageDetails(pageId: String) {
        _loading.value = true
        repository.fetchPageDetails(pageId, { pageData ->
            _page.value = pageData
            _loading.value = false
        }, { errorMsg ->
            _error.value = errorMsg
            _loading.value = false
        })
    }
}