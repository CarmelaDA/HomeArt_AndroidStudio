package com.carmelart.homeart.ui.bano

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class BanoViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is bano Fragment"
    }
    val text: LiveData<String> = _text
}