package com.flatcode.simplemultiapps.multipledelete.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MultiDelete : ViewModel() {

    private val _text = MutableLiveData<String>()
    val text: LiveData<String> get() = _text

    fun setText(s: String) {
        _text.value = s
    }
}