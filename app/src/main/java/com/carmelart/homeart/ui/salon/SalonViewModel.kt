package com.carmelart.homeart.ui.salon

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SalonViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "TELEVISIÓN"
    }
    val text: LiveData<String> = _text
}