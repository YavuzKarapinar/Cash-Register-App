package me.yavuz.delta_a_project.database.dao

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import me.yavuz.delta_a_project.model.Department

class DepartmentDAO(private val db: SQLiteDatabase) {

    private val groupDAO = GroupDAO(db)

    fun saveDepartment(group: String, name: String) {
        val values = ContentValues().apply {
            put("group_id", groupDAO.getGroupByName(group)?.id)
            put("name", name)
        }

        db.insert("department", null, values)
    }

    fun getDepartmentByName(name: String): Department? {
        val sql = "SELECT id, group_id, name FROM `department` WHERE name = ?"

        db.rawQuery(sql, arrayOf(name)).use {
            if (it.moveToFirst()) {
                return Department(it.getInt(0), it.getInt(1), it.getString(2))
            }
        }

        return null
    }

    fun getDepartments(): List<Department> {
        val departments = mutableListOf<Department>()
        val sql = "SELECT id, group_id, name FROM `department`"
        db.rawQuery(sql, null).use {
            while (it.moveToNext()) {
                departments.add(Department(it.getInt(0), it.getInt(1), it.getString(2)))
            }
        }

        return departments
    }

}