package me.yavuz.delta_a_project.database

import android.content.ContentValues
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

        //report
        db?.execSQL(DatabaseConstants.CREATE_REPORT_Z_TABLE_QUERY)
        db?.execSQL(DatabaseConstants.CREATE_REPORT_X_TABLE_QUERY)

        //inserting initial values
        initialValues(db)
    }

    private fun initialValues(db: SQLiteDatabase?) {
        val admin = ContentValues().apply { put("name", "Admin") }
        db?.insert("user_type", null, admin)
        val staff = ContentValues().apply { put("name", "Staff") }
        db?.insert("user_type", null, staff)

        val sellingType1 = ContentValues().apply { put("name", "Cash") }
        db?.insert("selling_process_type", null, sellingType1)
        val sellingType2 = ContentValues().apply { put("name", "Card") }
        db?.insert("selling_process_type", null, sellingType2)
        val sellingType3 = ContentValues().apply { put("name", "Other") }
        db?.insert("selling_process_type", null, sellingType3)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        when (oldVersion) {
            1 -> {
                if (newVersion >= 2) {
                    db?.execSQL("ALTER TABLE `selling_process` ADD COLUMN z_id INTEGER DEFAULT 1")
                    db?.execSQL(DatabaseConstants.CREATE_REPORT_Z_TABLE_QUERY)
                    db?.execSQL(DatabaseConstants.CREATE_REPORT_X_TABLE_QUERY)
                }
            }
        }
    }

    override fun onConfigure(db: SQLiteDatabase?) {
        super.onConfigure(db)
        db?.setForeignKeyConstraintsEnabled(true)
    }

}