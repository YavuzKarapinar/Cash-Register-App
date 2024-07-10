package me.yavuz.delta_a_project.database.dao

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import me.yavuz.delta_a_project.model.Tax

class TaxDAO(private val db: SQLiteDatabase) {

    fun getTaxes(): List<Tax> {
        val taxes = mutableListOf<Tax>()
        val sql = "SELECT id, name, value FROM taxes"
        db.rawQuery(sql, null).use {
            while (it.moveToNext()) {
                taxes.add(Tax(it.getInt(0), it.getString(1), it.getDouble(2)))
            }
        }

        return taxes
    }

    fun getTaxByName(name: String): Tax? {
        val sql = "SELECT id, name, value FROM taxes WHERE name = ?"

        db.rawQuery(sql, arrayOf(name)).use {
            if (it.moveToFirst()) {
                return Tax(it.getInt(0), it.getString(1), it.getDouble(2))
            }
        }

        return null
    }

    fun saveTax(name: String, value: Double) {
        val values = ContentValues().apply {
            put("name", name)
            put("value", value)
        }

        db.insert("taxes", null, values)
    }
}