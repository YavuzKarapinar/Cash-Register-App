package me.yavuz.delta_a_project.database.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.yavuz.delta_a_project.database.DbHelper
import me.yavuz.delta_a_project.database.dao.TaxDAO
import me.yavuz.delta_a_project.model.Tax

class TaxRepository(context: Context) {
    private val dbHelper = DbHelper.getInstance(context)
    private val taxDAO = TaxDAO(dbHelper.writableDatabase)

    suspend fun getTaxes(): List<Tax> {
        return withContext(Dispatchers.IO) {
            taxDAO.getTaxes()
        }
    }

    suspend fun getTaxByName(name: String): Tax? {
        return withContext(Dispatchers.IO) {
            taxDAO.getTaxByName(name)
        }
    }

    fun getTaxById(id: Int): Tax? {
        return taxDAO.getTaxById(id)
    }

    fun isTaxExists(name: String): Boolean {
        return taxDAO.isTaxExists(name)
    }

    suspend fun saveTax(name: String, value: Double) {
        withContext(Dispatchers.IO) {
            taxDAO.saveTax(name, value)
        }
    }
}