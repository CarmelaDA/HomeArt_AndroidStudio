package com.carmelart.homeart.ui.seguridad

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class SeguridadViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "ALARMA"
    }
    val text: LiveData<String> = _text
}