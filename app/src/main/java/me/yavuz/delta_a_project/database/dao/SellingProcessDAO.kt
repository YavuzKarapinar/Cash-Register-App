package me.yavuz.delta_a_project.database.dao

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import me.yavuz.delta_a_project.model.SellingProcess

class SellingProcessDAO(private val db: SQLiteDatabase) {

    fun getSellingProcessById(id: Int): SellingProcess? {
        val sql =
            "SELECT id, quantity, price_sell, amount, user_id, selling_process_type_id, product_id " +
                    "FROM selling_process " +
                    "WHERE id = ?"

        db.rawQuery(sql, arrayOf(id.toString())).use {
            if (it.moveToFirst()) {
                return SellingProcess(
                    it.getInt(0),
                    it.getInt(1),
                    it.getDouble(2),
                    it.getDouble(3),
                    it.getInt(4),
                    it.getInt(5),
                    it.getInt(6)
                )
            }
        }

        return null
    }

    fun saveSellingProcess(sellingProcess: SellingProcess) {
        val values = ContentValues().apply {
            put("quantity", sellingProcess.quantity)
            put("price_sell", sellingProcess.priceSell)
            put("amount", sellingProcess.amount)
            put("user_id", sellingProcess.userId)
            put("selling_process_type_id", sellingProcess.sellingProcessTypeId)
            put("product_id", sellingProcess.productId)
        }

        db.insert("selling_process", null, values)
    }
}