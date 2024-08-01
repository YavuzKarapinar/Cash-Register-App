package me.yavuz.delta_a_project.database.dao

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

class ReportZDAO(private val db: SQLiteDatabase) {

    fun getLastZNumber(): Int {
        val sql = "SELECT MAX(id) FROM report_z"

        val values = ContentValues().apply {
            put("timestamp", System.currentTimeMillis())
        }

        db.rawQuery(sql, null).use { cursor ->
            if (cursor.moveToFirst()) {
                return if (cursor.getInt(0) > 0) cursor.getInt(0)
                else db.insert("report_z", null, values).toInt()
            }
        }

        return 0
    }

    fun insertNewReportZ() {
        val values = ContentValues().apply {
            val zNumber = getLastZNumber()
            if (zNumber > 0) {
                put("timestamp", System.currentTimeMillis())
            }
        }

        db.insert("report_z", null, values)
    }
}