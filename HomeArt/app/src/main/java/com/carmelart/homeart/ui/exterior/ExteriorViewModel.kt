package com.carmelart.homeart.ui.exterior

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class ExteriorViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is exterior Fragment"
    }
    val text: LiveData<String> = _text
}