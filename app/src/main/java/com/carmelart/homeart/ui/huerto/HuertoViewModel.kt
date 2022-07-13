package com.carmelart.homeart.ui.huerto

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HuertoViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "HUERTO"
    }
    val text: LiveData<String> = _text
}