package me.yavuz.delta_a_project.database.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.yavuz.delta_a_project.database.DbHelper
import me.yavuz.delta_a_project.database.dao.UserDAO
import me.yavuz.delta_a_project.model.User
import me.yavuz.delta_a_project.model.UserType

class UserRepository(context: Context) {
    private val db = DbHelper.getInstance(context).writableDatabase
    private val userDAO = UserDAO(db)

    suspend fun getUserByNameAndPassword(name: String, password: String): User? {
        return withContext(Dispatchers.IO) {
            userDAO.getUserByNameAndPassword(name, password)
        }
    }
    suspend fun getUserById(id: Int): User? {
        return withContext(Dispatchers.IO) {
            userDAO.getUserById(id)
        }
    }

    fun isUserExists(name: String): Boolean {
        return userDAO.isUserExists(name)
    }

    suspend fun getUsers(): List<User> {
        return withContext(Dispatchers.IO) {
            userDAO.getUsers()
        }
    }

    suspend fun saveUser(userTypeName: String, name: String, password: String) {
        withContext(Dispatchers.IO) {
            userDAO.saveUser(userTypeName, name, password)
        }
    }

    suspend fun getUserTypes(): List<UserType> {
        return withContext(Dispatchers.IO) {
            userDAO.getUserTypes()
        }
    }

}