package me.yavuz.delta_a_project.database.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.yavuz.delta_a_project.database.DbHelper
import me.yavuz.delta_a_project.database.dao.ReportZDAO

class ReportZRepository(context: Context) {
    private val dbHelper = DbHelper.getInstance(context)
    private val reportZDAO = ReportZDAO(dbHelper.writableDatabase)

    suspend fun getLastZNumber(): Int {
        return withContext(Dispatchers.IO) {
            reportZDAO.getLastZNumber()
        }
    }

    suspend fun insertReportZ() {
        withContext(Dispatchers.IO) {
            reportZDAO.insertNewReportZ()
        }
    }
}