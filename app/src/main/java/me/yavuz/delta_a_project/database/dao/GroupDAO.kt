package me.yavuz.delta_a_project.database.dao

import android.content.ContentValues
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import me.yavuz.delta_a_project.model.Group

class GroupDAO(private val db: SQLiteDatabase) {

    fun getGroups(): List<Group> {
        val groups = mutableListOf<Group>()
        val sql = "SELECT id, name FROM groups"

        db.rawQuery(sql, null).use {
            while (it.moveToNext()) {
                groups.add(Group(it.getInt(0), it.getString(1)))
            }
        }

        return groups
    }

    fun getGroupById(id: Int): Group? {
        val sql = "SELECT id, name FROM groups WHERE id = ?"

        db.rawQuery(sql, arrayOf(id.toString())).use {
            if (it.moveToFirst()) {
                return Group(it.getInt(0), it.getString(1))
            }
        }

        return null
    }

    fun getGroupByName(name: String): Group? {
        val sql = "SELECT id, name FROM groups WHERE name = ?"

        db.rawQuery(sql, arrayOf(name)).use {
            if (it.moveToFirst()) {
                return Group(it.getInt(0), it.getString(1))
            }
        }

        return null
    }

    fun isGroupExists(name: String): Boolean {
        val sql =
            "SELECT * FROM groups WHERE name = ?"

        db.rawQuery(sql, arrayOf(name)).use {
            return it.moveToFirst()
        }
    }

    fun saveGroup(groupName: String) {
        val values = ContentValues().apply {
            put("name", groupName)
        }

        db.insert("groups", null, values)
    }

    fun updateGroup(group: Group) {
        val values = ContentValues().apply {
            put("name", group.name)
        }
        db.update("groups", values, "id = ?", arrayOf(group.id.toString()))
    }

    @Throws(SQLiteConstraintException::class)
    fun deleteGroup(group: Group) {
        try {
            db.delete("groups", "id = ?", arrayOf(group.id.toString()))
        } catch (e: SQLiteConstraintException) {
            throw e
        }
    }
}