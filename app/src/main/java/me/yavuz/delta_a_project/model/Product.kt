package me.yavuz.delta_a_project.model

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val quantity: Int,
    val tax: Double,
    val productNumber: Int,
    val departmentId: Int
)