package com.carmelart.homeart.ui.garaje

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GarajeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "ILUMINACIÓN"
    }
    val text: LiveData<String> = _text
}