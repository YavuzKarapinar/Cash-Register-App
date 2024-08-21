package me.yavuz.delta_a_project.model

/**
 * Products are items that is used for selling.
 * Product data class representing product database table
 *
 * @property id Product's unique primary key
 * @property name Product's name. Representing table's unique name column database value
 * @property price Product's price for sell
 * @property stock Product's stock for selling. It should be greater than or equal to 0 and can be updated.
 * @property productNumber Product's product number. Generally used for barcode
 * @property taxId Product's tax id.This is a foreign key referencing the tax table
 * @property departmentId Product's department id. This is a foreign key referencing the department table
 */
data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    var stock: Int,
    val productNumber: Int,
    val taxId: Int,
    val departmentId: Int
)