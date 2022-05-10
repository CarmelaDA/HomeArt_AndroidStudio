package com.carmelart.homeart.ui.bano

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BanoViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is bano Fragment"
    }
    val text: LiveData<String> = _text
}