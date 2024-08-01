package me.yavuz.delta_a_project.database.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.yavuz.delta_a_project.database.DbHelper
import me.yavuz.delta_a_project.database.dao.SellingProcessDAO
import me.yavuz.delta_a_project.model.SellingProcess
import me.yavuz.delta_a_project.model.SellingProcessType

class SellingProcessRepository(context: Context) {
    private val dbHelper = DbHelper.getInstance(context)
    private val sellingProcessDAO = SellingProcessDAO(dbHelper.writableDatabase)

    suspend fun getSellingProcessById(id: Int): SellingProcess? {
        return withContext(Dispatchers.IO) {
            sellingProcessDAO.getSellingProcessById(id)
        }
    }

    suspend fun getSellingProcessesByZReportId(zId: Int): List<SellingProcess> {
        return withContext(Dispatchers.IO) {
            sellingProcessDAO.getSellingProcessListByZReportId(zId)
        }
    }

    suspend fun saveSellingProcess(sellingProcess: SellingProcess): Long {
        return withContext(Dispatchers.IO) {
            sellingProcessDAO.saveSellingProcess(sellingProcess)
        }
    }

    suspend fun getSellingTypeById(id: Int): SellingProcessType? {
        return withContext(Dispatchers.IO) {
            sellingProcessDAO.getSellingTypeById(id)
        }
    }
}