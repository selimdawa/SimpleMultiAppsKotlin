package com.flatcode.simplemultiapps.MultipleDelete.Model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MultiDelete : ViewModel() {
    var text = MutableLiveData<String>()

    fun setText(s: String) {
        text.value = s
    }
}