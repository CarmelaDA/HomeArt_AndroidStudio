package com.carmelart.homeart.ui.iluminacion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class IluminacionViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "SALÓN"
    }
    val text: LiveData<String> = _text
}