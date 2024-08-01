package me.yavuz.delta_a_project.model

data class ReportItem(
    val type: String,
    var quantity: Int,
    var sale: Double,
    var returnSale: Double
)