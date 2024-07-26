package me.yavuz.delta_a_project.database.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.yavuz.delta_a_project.database.DbHelper
import me.yavuz.delta_a_project.database.dao.DepartmentDAO
import me.yavuz.delta_a_project.model.Department

class DepartmentRepository(context: Context) {
    private val dbHelper = DbHelper.getInstance(context)
    private val departmentDAO = DepartmentDAO(dbHelper.writableDatabase)

    suspend fun getDepartments(): List<Department> {
        return withContext(Dispatchers.IO) {
            departmentDAO.getDepartments()
        }
    }

    suspend fun getDepartmentByName(name: String): Department? {
        return withContext(Dispatchers.IO) {
            departmentDAO.getDepartmentByName(name)
        }
    }

    fun isDepartmentExists(name: String): Boolean {
        return departmentDAO.isDepartmentExists(name)
    }

    suspend fun saveDepartment(group: String, name: String) {
        withContext(Dispatchers.IO) {
            departmentDAO.saveDepartment(group, name)
        }
    }
}