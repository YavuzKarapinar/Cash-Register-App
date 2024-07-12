package me.yavuz.delta_a_project.database.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.yavuz.delta_a_project.database.DbHelper
import me.yavuz.delta_a_project.database.dao.SellingProcessDAO
import me.yavuz.delta_a_project.model.SellingProcess

class SellingProcessRepository(context: Context) {
    private val dbHelper = DbHelper.getInstance(context)
    private val sellingProcessDAO = SellingProcessDAO(dbHelper.writableDatabase)

    suspend fun getSellingProcessById(id: Int): SellingProcess? {
        return withContext(Dispatchers.IO) {
            sellingProcessDAO.getSellingProcessById(id)
        }
    }

    suspend fun saveSellingProcess(sellingProcess: SellingProcess) {
        withContext(Dispatchers.IO) {
            sellingProcessDAO.saveSellingProcess(sellingProcess)
        }
    }

}