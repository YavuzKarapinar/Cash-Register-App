package me.yavuz.delta_a_project.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

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

}