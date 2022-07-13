package com.carmelart.homeart.ui.cocina

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CocinaViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "ILUMINACIÓN"
    }
    val text: LiveData<String> = _text
}