package com.example.licenta_copie.ModelView

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {
    val car_id = MutableLiveData<String>()
    var user_id = MutableLiveData<String>()
    var user_email = MutableLiveData<String>()
}