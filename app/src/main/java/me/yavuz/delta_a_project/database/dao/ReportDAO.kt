package me.yavuz.delta_a_project.database.dao

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

class ReportDAO(private val db: SQLiteDatabase) {

    fun getLastZNumber(): Int {
        val sql = "SELECT MAX(id) FROM report_z"
        db.rawQuery(sql, null).use { cursor ->
            if (cursor.moveToFirst()) {
                return cursor.getInt(0).takeIf { it > 0 } ?: 0
            }
        }
        return 0
    }

    fun insertNewReportZ(): Int {
        val values = ContentValues().apply {
            put("timestamp", System.currentTimeMillis())
        }
        return db.insert("report_z", null, values).toInt()
    }

    fun getLastXNumber(): Int {
        val sql = "SELECT MAX(id) FROM report_x"
        db.rawQuery(sql, null).use { cursor ->
            if (cursor.moveToFirst()) {
                return cursor.getInt(0).takeIf { it > 0 } ?: 0
            }
        }

        return 0
    }

    fun insertReportX(zId: Int): Int {
        val values = ContentValues().apply {
            put("timestamp", System.currentTimeMillis())
            put("z_id", zId)
        }
        return db.insert("report_x", null, values).toInt()
    }
}