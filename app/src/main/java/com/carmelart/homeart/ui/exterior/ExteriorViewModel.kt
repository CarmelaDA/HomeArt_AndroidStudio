package com.carmelart.homeart.ui.exterior

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ExteriorViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is exterior Fragment"
    }
    val text: LiveData<String> = _text
}