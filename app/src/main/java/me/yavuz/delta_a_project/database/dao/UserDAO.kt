package me.yavuz.delta_a_project.database.dao

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import me.yavuz.delta_a_project.model.User
import me.yavuz.delta_a_project.model.UserType

class UserDAO(private val db: SQLiteDatabase) {

    fun getUserByNameAndPassword(name: String, password: String): User? {
        val sql =
            "SELECT id, user_type_id, name, password FROM users WHERE name=? and password=?"
        db.rawQuery(sql, arrayOf(name, password)).use {
            if (it.moveToFirst()) {
                val userType = getUserTypeById(it.getInt(1))
                return User(it.getInt(0), userType!!.name, it.getString(2), it.getString(3))
            }
        }
        return null
    }

    fun saveUser(userTypeName: String, name: String, password: String) {
        val userType = getUserTypeByName(userTypeName)
        val values = ContentValues().apply {
            put("user_type_id", userType?.id)
            put("name", name)
            put("password", password)
        }
        db.insert("users", null, values)

    }

    fun getUsers(): List<User> {
        val users = mutableListOf<User>()
        val sql = """
        SELECT s.id, s.name, s.password, ut.name AS user_type_name
        FROM users s
        INNER JOIN user_type ut ON s.user_type_id = ut.id
        """

        db.rawQuery(sql, null).use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(0)
                    val name = cursor.getString(1)
                    val password = cursor.getString(2)
                    val userTypeName = cursor.getString(3)
                    users.add(User(id, name, password, userTypeName))
                } while (cursor.moveToNext())
            }
        }
        return users
    }

    fun getUserTypes(): List<UserType> {
        val sql = "SELECT id, name FROM user_type"
        val list = mutableListOf<UserType>()
        db.rawQuery(sql, null).use { cursor ->
            while (cursor.moveToNext()) {
                list.add(UserType(cursor.getInt(0), cursor.getString(1)))
            }
        }
        return list
    }

    private fun getUserTypeByName(name: String): UserType? {
        val sql = "SELECT id, name FROM user_type WHERE name = ?"

        db.rawQuery(sql, arrayOf(name)).use {
            if (it.moveToFirst()) {
                return UserType(it.getInt(0), it.getString(1))
            }
        }

        return null
    }

    private fun getUserTypeById(id: Int): UserType? {
        val sql = "SELECT id, name FROM user_type WHERE id = ?"

        db.rawQuery(sql, arrayOf(id.toString())).use {
            if (it.moveToFirst()) {
                return UserType(it.getInt(0), it.getString(1))
            }
        }

        return null
    }
}