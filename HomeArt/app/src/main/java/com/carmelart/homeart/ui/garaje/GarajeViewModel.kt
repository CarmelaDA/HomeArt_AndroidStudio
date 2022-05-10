package com.carmelart.homeart.ui.garaje

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class GarajeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is garaje Fragment"
    }
    val text: LiveData<String> = _text
}