package com.carmelart.homeart.ui.tiempo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TiempoViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "INTERIOR"
    }
    val text: LiveData<String> = _text
}