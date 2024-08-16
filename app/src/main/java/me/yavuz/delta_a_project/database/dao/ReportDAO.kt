package me.yavuz.delta_a_project.database.dao

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import me.yavuz.delta_a_project.model.ReportX
import me.yavuz.delta_a_project.model.ReportZ

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

    fun getAllReportZ(): List<ReportZ> {
        val list = mutableListOf<ReportZ>()
        val sql =
            "SELECT id, timestamp FROM report_z"

        db.rawQuery(sql, null).use {
            while (it.moveToNext()) {
                list.add(ReportZ(it.getInt(0), it.getLong(1)))
            }
        }

        return list
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

    fun getAllReportX(): List<ReportX> {
        val list = mutableListOf<ReportX>()
        val sql =
            "SELECT id, z_id, timestamp FROM report_x"

        db.rawQuery(sql, null).use {
            while (it.moveToNext()) {
                list.add(ReportX(it.getInt(0), it.getInt(1), it.getLong(2)))
            }
        }

        return list
    }

    fun insertReportX(zId: Int): Int {
        val values = ContentValues().apply {
            put("timestamp", System.currentTimeMillis())
            put("z_id", zId)
        }
        return db.insert("report_x", null, values).toInt()
    }
}