package me.yavuz.delta_a_project.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _data = MutableLiveData<Int>()
    val data: LiveData<Int> get() = _data

    fun setData(data: Int) {
        _data.value = data
    }
}