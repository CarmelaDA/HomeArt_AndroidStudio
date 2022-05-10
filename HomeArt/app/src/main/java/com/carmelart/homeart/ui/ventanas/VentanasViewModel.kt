package com.carmelart.homeart.ui.ventanas

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class VentanasViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "PARCELA"
    }
    val text: LiveData<String> = _text
}