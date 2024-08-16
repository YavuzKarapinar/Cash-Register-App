package me.yavuz.delta_a_project.database.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.yavuz.delta_a_project.database.DbHelper
import me.yavuz.delta_a_project.database.dao.ReportDAO
import me.yavuz.delta_a_project.model.ReportX
import me.yavuz.delta_a_project.model.ReportZ

class ReportRepository(context: Context) {
    private val dbHelper = DbHelper.getInstance(context)
    private val reportDAO = ReportDAO(dbHelper.writableDatabase)

    suspend fun getLastZNumber(): Int {
        return withContext(Dispatchers.IO) {
            reportDAO.getLastZNumber()
        }
    }

    suspend fun getAllReportZ(): List<ReportZ> {
        return withContext(Dispatchers.IO) {
            reportDAO.getAllReportZ()
        }
    }

    suspend fun insertReportZ(): Int {
        return withContext(Dispatchers.IO) {
            reportDAO.insertNewReportZ()
        }
    }

    suspend fun getLastXNumber(): Int {
        return withContext(Dispatchers.IO) {
            reportDAO.getLastXNumber()
        }
    }

    suspend fun getAllReportX(): List<ReportX> {
        return withContext(Dispatchers.IO) {
            reportDAO.getAllReportX()
        }
    }

    suspend fun insertReportX(zId: Int): Int {
        return withContext(Dispatchers.IO) {
            reportDAO.insertReportX(zId)
        }
    }
}