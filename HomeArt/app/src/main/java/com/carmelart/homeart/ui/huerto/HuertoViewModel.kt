package com.carmelart.homeart.ui.huerto

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class HuertoViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is huerto Fragment"
    }
    val text: LiveData<String> = _text
}