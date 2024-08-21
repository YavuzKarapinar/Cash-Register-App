package me.yavuz.delta_a_project.model

/**
 * Report Z is a report type that can be taken all time.
 * Representing report_z database table
 *
 * @property id Report Z's unique id
 * @property timestamp Report Z's timestamp that representing its created time
 */
data class ReportZ(
    val id: Int,
    val timestamp: Long
)