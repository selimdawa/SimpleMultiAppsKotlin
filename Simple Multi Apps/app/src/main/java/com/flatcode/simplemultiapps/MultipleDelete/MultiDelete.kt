package com.flatcode.simplemultiapps.MultipleDelete

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MultiDelete : ViewModel() {
    //Create get text method
    //Initialize variable
    var text = MutableLiveData<String>()

    //Create set text method
    fun setText(s: String) {
        //Set value
        text.value = s
    }
}