package me.yavuz.delta_a_project.model

data class SellingProcess(
    val id: Int,
    val quantity: Int,
    val priceSell: Double,
    val amount: Double = quantity * priceSell,
    val sellingFormat: String,
    val userId: Int,
    val sellingProcessTypeId: Int,
    val productId: Int
)