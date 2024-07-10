package me.yavuz.delta_a_project.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.yavuz.delta_a_project.database.repository.UserRepository
import me.yavuz.delta_a_project.model.User
import me.yavuz.delta_a_project.model.UserType

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = UserRepository(application)

    fun getUserByNameAndPassword(name: String, password: String): LiveData<User?> {
        val userLiveData = MutableLiveData<User?>()
        viewModelScope.launch {
            val user = repository.getUserByNameAndPassword(name, password)
            userLiveData.postValue(user)
        }

        return userLiveData
    }

    fun saveUser(userTypeName: String, name: String, password: String) {
        viewModelScope.launch {
            repository.saveUser(userTypeName, name, password)
        }
    }

    fun getUsers(): LiveData<List<User>> {
        val usersLiveData = MutableLiveData<List<User>>()
        viewModelScope.launch {
            val users = repository.getUsers()
            usersLiveData.postValue(users)
        }
        return usersLiveData
    }

    fun getUserTypes(): LiveData<List<UserType>> {
        val userTypesLiveData = MutableLiveData<List<UserType>>()
        viewModelScope.launch {
            val userTypes = repository.getUserTypes()
            userTypesLiveData.postValue(userTypes)
        }
        return userTypesLiveData
    }
}