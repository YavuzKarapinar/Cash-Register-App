package me.yavuz.delta_a_project.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
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
            Log.d("TAG", "saveUser: $it")
            if (it.moveToFirst()) {
                val userTypeId = it.getInt(0)
                val values = ContentValues().apply {
                    put("user_type_id", userTypeId)
                    put("name", name)
                    put("password", password)
                }
                val newRowId = db.insert("users", null, values)
                if (newRowId == -1L) {
                    Log.e("TAG", "Error inserting user: $values")
                } else {
                    Log.d("TAG", "User inserted with ID: $newRowId")
                }
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
}