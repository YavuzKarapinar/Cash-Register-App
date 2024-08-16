package me.yavuz.delta_a_project.database.dao

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import me.yavuz.delta_a_project.model.SellingProcess
import me.yavuz.delta_a_project.model.SellingProcessType

class SellingProcessDAO(private val db: SQLiteDatabase) {

    fun getSellingProcessById(id: Int): SellingProcess? {
        val sql =
            "SELECT id, quantity, price_sell, amount, selling_format, z_id, x_id, user_id, " +
                    "selling_process_type_id, product_id " +
                    "FROM selling_process " +
                    "WHERE id = ?"

        db.rawQuery(sql, arrayOf(id.toString())).use {
            if (it.moveToFirst()) {
                return SellingProcess(
                    it.getInt(0),
                    it.getInt(1),
                    it.getDouble(2),
                    it.getDouble(3),
                    it.getString(4),
                    it.getInt(5),
                    it.getInt(6),
                    it.getInt(7),
                    it.getInt(8),
                    it.getInt(9)
                )
            }
        }

        return null
    }

    fun getSellingProcessListByZReportId(zId: Int): List<SellingProcess> {
        val list = mutableListOf<SellingProcess>()
        val sql =
            "SELECT id, quantity, price_sell, amount, selling_format, z_id, x_id, user_id, " +
                    "selling_process_type_id, product_id " +
                    "FROM selling_process " +
                    "WHERE z_id = ?"

        db.rawQuery(sql, arrayOf(zId.toString())).use {
            while (it.moveToNext()) {
                list.add(
                    SellingProcess(
                        it.getInt(0),
                        it.getInt(1),
                        it.getDouble(2),
                        it.getDouble(3),
                        it.getString(4),
                        it.getInt(5),
                        it.getInt(6),
                        it.getInt(7),
                        it.getInt(8),
                        it.getInt(9)
                    )
                )
            }
        }

        return list
    }

    fun getSellingProcessListByXAndZId(xId: Int, zId: Int): List<SellingProcess> {
        val list = mutableListOf<SellingProcess>()
        val sql =
            "SELECT id, quantity, price_sell, amount, selling_format, z_id, x_id, user_id, " +
                    "selling_process_type_id, product_id " +
                    "FROM selling_process " +
                    "WHERE x_id <= ? and z_id = ?"

        db.rawQuery(sql, arrayOf(xId.toString(), zId.toString())).use {
            while (it.moveToNext()) {
                list.add(
                    SellingProcess(
                        it.getInt(0),
                        it.getInt(1),
                        it.getDouble(2),
                        it.getDouble(3),
                        it.getString(4),
                        it.getInt(5),
                        it.getInt(6),
                        it.getInt(7),
                        it.getInt(8),
                        it.getInt(9)
                    )
                )
            }
        }

        return list
    }

    fun saveSellingProcess(sellingProcess: SellingProcess): Long {
        val values = ContentValues().apply {
            put("quantity", sellingProcess.quantity)
            put("price_sell", sellingProcess.priceSell)
            put("amount", sellingProcess.amount)
            put("selling_format", sellingProcess.sellingFormat)
            put("z_id", sellingProcess.zId)
            put("x_id", sellingProcess.xId)
            put("user_id", sellingProcess.userId)
            put("selling_process_type_id", sellingProcess.sellingProcessTypeId)
            put("product_id", sellingProcess.productId)
        }

        return db.insert("selling_process", null, values)
    }

    fun getSellingTypeById(id: Int): SellingProcessType? {
        val sql =
            "SELECT id, name " +
                    "FROM selling_process_type " +
                    "WHERE id = ?"

        db.rawQuery(sql, arrayOf(id.toString())).use {
            if (it.moveToFirst()) {
                return SellingProcessType(
                    it.getInt(0),
                    it.getString(1)
                )
            }
        }

        return null
    }
}