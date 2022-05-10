package com.carmelart.homeart.ui.cocina

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class CocinaViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "COCINA"
    }
    val text: LiveData<String> = _text
}