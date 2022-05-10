package com.carmelart.homeart.ui.garaje

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GarajeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is garaje Fragment"
    }
    val text: LiveData<String> = _text
}