package com.carmelart.homeart.ui.tiempo

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class TiempoViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "INTERIOR"
    }
    val text: LiveData<String> = _text
}