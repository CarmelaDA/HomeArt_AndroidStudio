package com.carmelart.homeart.ui.ventanas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VentanasViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "PARCELA"
    }
    val text: LiveData<String> = _text
}