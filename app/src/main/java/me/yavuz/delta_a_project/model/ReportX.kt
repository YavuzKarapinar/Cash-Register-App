package me.yavuz.delta_a_project.model

/**
 * Report X is a report type that can be taken all time.
 * Representing report_x database table
 *
 * @property id Report X's unique id
 * @property zId Report X's [ReportZ] id. This is a foreign key referencing the report_x table
 * @property timestamp Report X's timestamp that representing its created time
 */
data class ReportX(
    val id: Int,
    val zId: Int,
    val timestamp: Long
)