package me.yavuz.delta_a_project.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _data = MutableLiveData<Int>()

    /**
     * shared data's live value
     */
    val data: LiveData<Int> get() = _data

    /**
     * Data for sharing between activities
     *
     * @param data data for sharing
     */
    fun setData(data: Int) {
        _data.value = data
    }
}