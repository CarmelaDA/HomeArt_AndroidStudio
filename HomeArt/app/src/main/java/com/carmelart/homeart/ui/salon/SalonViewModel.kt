package com.carmelart.homeart.ui.salon

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class SalonViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is salon Fragment"
    }
    val text: LiveData<String> = _text
}