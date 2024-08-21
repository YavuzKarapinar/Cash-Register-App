package me.yavuz.delta_a_project.model

/**
 * Item for showing information in the report alert dialog
 *
 * @property type Item's type. It can be any type. Such as Selling Process Type **Other**
 * @property quantity Item's quantity. Can be changed after created
 * @property sale Item's sale. Can be changed after created. Shows how many items sold
 * @property returnSale Item's return value. Can be changed after created. Shows how many items returned
 */
data class ReportItem(
    val type: String,
    var quantity: Int,
    var sale: Double,
    var returnSale: Double
)