package com.carmelart.homeart.ui.iluminacion

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class IluminacionViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "SALÓN"
    }
    val text: LiveData<String> = _text
}