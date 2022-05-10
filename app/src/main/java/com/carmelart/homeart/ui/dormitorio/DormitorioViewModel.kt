package com.carmelart.homeart.ui.dormitorio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DormitorioViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dormitorio Fragment"
    }
    val text: LiveData<String> = _text
}