package com.carmelart.homeart.ui.dormitorio

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class DormitorioViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dormitorio Fragment"
    }
    val text: LiveData<String> = _text
}