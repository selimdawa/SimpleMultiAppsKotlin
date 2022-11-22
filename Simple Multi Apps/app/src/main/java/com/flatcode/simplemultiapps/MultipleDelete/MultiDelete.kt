package com.flatcode.simplemultiapps.MultipleDelete

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.appcompat.app.AppCompatActivity
import android.app.Activity
import android.os.Bundle
import com.flatcode.simplemultiapps.Unit.THEME
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProviders
import androidx.fragment.app.FragmentActivity
import android.view.View.OnLongClickListener
import android.view.MenuInflater
import com.flatcode.simplemultiapps.R
import androidx.lifecycle.LifecycleOwner
import android.widget.Toast

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