package com.carmelart.homeart.ui.configuracion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConfiguracionViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "AJUSTES"
    }
    val text: LiveData<String> = _text
}