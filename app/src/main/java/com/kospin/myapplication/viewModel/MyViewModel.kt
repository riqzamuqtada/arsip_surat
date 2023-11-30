package com.kospin.myapplication.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel() {

    private var _username = MutableLiveData<String>()
    val username: LiveData<String>
        get() = _username
    fun setUser(setUsername: String){
        _username.value = setUsername
    }
}