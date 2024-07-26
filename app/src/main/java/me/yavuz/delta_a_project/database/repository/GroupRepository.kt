package me.yavuz.delta_a_project.database.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.yavuz.delta_a_project.database.DbHelper
import me.yavuz.delta_a_project.database.dao.GroupDAO
import me.yavuz.delta_a_project.model.Group

class GroupRepository(context: Context) {

    private val db = DbHelper.getInstance(context)
    private val groupDAO = GroupDAO(db.writableDatabase)

    suspend fun getGroups(): List<Group> {
        return withContext(Dispatchers.IO) {
            groupDAO.getGroups()
        }
    }

    suspend fun getGroupById(id: Int): Group? {
        return withContext(Dispatchers.IO) {
            groupDAO.getGroupById(id)
        }
    }

    suspend fun getGroupByName(name: String): Group? {
        return withContext(Dispatchers.IO) {
            groupDAO.getGroupByName(name)
        }
    }

    fun isGroupExists(name: String): Boolean {
        return groupDAO.isGroupExists(name)
    }

    suspend fun saveGroup(name: String) {
        withContext(Dispatchers.IO) {
            groupDAO.saveGroup(name)
        }
    }
}