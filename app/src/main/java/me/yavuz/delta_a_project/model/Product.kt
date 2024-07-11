package me.yavuz.delta_a_project.model

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    var stock: Int,
    val productNumber: Int,
    val taxId: Int,
    val departmentId: Int
)