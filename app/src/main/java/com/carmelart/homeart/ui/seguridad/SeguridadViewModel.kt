package com.carmelart.homeart.ui.seguridad

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SeguridadViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "ALARMA"
    }
    val text: LiveData<String> = _text
}