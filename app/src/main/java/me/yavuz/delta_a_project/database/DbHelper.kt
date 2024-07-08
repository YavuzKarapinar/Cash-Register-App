package me.yavuz.delta_a_project.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import me.yavuz.delta_a_project.model.Department
import me.yavuz.delta_a_project.model.Group
import me.yavuz.delta_a_project.model.User
import me.yavuz.delta_a_project.model.UserType

class DbHelper private constructor(context: Context) :
    SQLiteOpenHelper(
        context,
        DatabaseConstants.DATABASE_NAME,
        null,
        DatabaseConstants.DATABASE_VERSION
    ) {

    companion object {
        @Volatile
        private var instance: DbHelper? = null

        fun getInstance(context: Context): DbHelper {
            return instance ?: synchronized(this) {
                instance ?: DbHelper(context.applicationContext).also { instance = it }
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //master data
        db?.execSQL(DatabaseConstants.CREATE_GROUP_TABLE_QUERY)
        db?.execSQL(DatabaseConstants.CREATE_DEPARTMENT_TABLE_QUERY)
        db?.execSQL(DatabaseConstants.CREATE_PRODUCTS_TABLE_QUERY)
        db?.execSQL(DatabaseConstants.CREATE_TAXES_TABLE_QUERY)

        //staff
        db?.execSQL(DatabaseConstants.CREATE_USERS_TABLE_QUERY)
        db?.execSQL(DatabaseConstants.CREATE_USER_TYPE_TABLE_QUERY)

        //selling process
        db?.execSQL(DatabaseConstants.CREATE_SELLING_PROCESS_TABLE_QUERY)
        db?.execSQL(DatabaseConstants.CREATE_SELLING_PROCESS_TYPE_TABLE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
         db?.execSQL("DROP TABLE IF EXISTS `group`")
         db?.execSQL("DROP TABLE IF EXISTS `department`")
         db?.execSQL("DROP TABLE IF EXISTS `products`")
         db?.execSQL("DROP TABLE IF EXISTS `taxes`")
         db?.execSQL("DROP TABLE IF EXISTS `users`")
         db?.execSQL("DROP TABLE IF EXISTS `user_type`")
         db?.execSQL("DROP TABLE IF EXISTS `selling_process`")
         db?.execSQL("DROP TABLE IF EXISTS `selling_process_type`")
         onCreate(db)
    }

    override fun onConfigure(db: SQLiteDatabase?) {
        super.onConfigure(db)
        db?.setForeignKeyConstraintsEnabled(true)
    }

    fun checkUserExistence(name: String, password: String): Int {
        val sql =
            "SELECT id FROM users WHERE name=? and password=?"
        val db = this.readableDatabase
        db.rawQuery(sql, arrayOf(name, password)).use {
            if (it.moveToFirst()) {
                return it.getInt(0)
            }
        }
        return -1
    }

    fun getUserTypes(): List<UserType> {
        val sql = "SELECT id, name FROM user_type"
        val db = this.readableDatabase
        val list = mutableListOf<UserType>()
        db.rawQuery(sql, null).use { cursor ->
            while (cursor.moveToNext()) {
                list.add(UserType(cursor.getInt(0), cursor.getString(1)))
            }
        }
        return list
    }

    fun saveUser(userType: String, name: String, password: String) {
        val db = this.readableDatabase
        val sql = "SELECT id FROM user_type WHERE name = ?"
        db.rawQuery(sql, arrayOf(userType)).use {
            if (it.moveToFirst()) {
                val userTypeId = it.getInt(0)
                val values = ContentValues().apply {
                    put("user_type_id", userTypeId)
                    put("name", name)
                    put("password", password)
                }
                db.insert("users", null, values)
            }
        }
    }

    fun getUsers(): List<User> {
        val users = mutableListOf<User>()
        val db = this.readableDatabase
        val sql = """
        SELECT s.id, s.name, s.password, ut.name AS user_type_name
        FROM users s
        INNER JOIN user_type ut ON s.user_type_id = ut.id
    """
        db.rawQuery(sql, null).use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                    val password = cursor.getString(cursor.getColumnIndexOrThrow("password"))
                    val userTypeName =
                        cursor.getString(cursor.getColumnIndexOrThrow("user_type_name"))
                    users.add(User(id, name, password, userTypeName))
                } while (cursor.moveToNext())
            }
        }
        return users
    }

    fun saveGroup(groupName: String) {
        val db = this.readableDatabase
        val values = ContentValues().apply {
            put("name", groupName)
        }

        db.insert("`group`", null, values)
    }

    fun getGroups(): List<Group> {
        val groups = mutableListOf<Group>()
        val sql =
            "SELECT id, name FROM `group`"

        val db = this.readableDatabase

        db.rawQuery(sql, null).use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndexOrThrow("id"))
                val name = it.getString(it.getColumnIndexOrThrow("name"))
                groups.add(Group(id, name))
            }
        }

        return groups
    }

    fun getGroupById(id: Int): Group? {
        val db = this.readableDatabase
        val sql = "SELECT id, name FROM `group` WHERE id = ?"

        db.rawQuery(sql, arrayOf(id.toString())).use {
            if (it.moveToFirst()) {
                return Group(it.getInt(0), it.getString(1))
            }
        }

        return null
    }

    fun getGroupByName(name: String): Group? {
        val db = this.readableDatabase
        val sql = "SELECT id, name FROM `group` WHERE name = ?"

        db.rawQuery(sql, arrayOf(name)).use {
            if (it.moveToFirst()) {
                return Group(it.getInt(0), it.getString(1))
            }
        }

        return null
    }

    fun saveDepartment(group: String, name: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("group_id", getGroupByName(group)?.id)
            put("name", name)
        }

        db.insert("department", null, values)
    }

    fun getDepartmentByName(name: String): Department? {
        val db = this.readableDatabase
        val sql = "SELECT id, group_id, name FROM `department` WHERE name = ?"

        db.rawQuery(sql, arrayOf(name)).use {
            if (it.moveToFirst()) {
                return Department(it.getInt(0), it.getInt(1), it.getString(2))
            }
        }

        return null
    }
}