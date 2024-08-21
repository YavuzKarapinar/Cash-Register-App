package me.yavuz.delta_a_project.model

/**
 * Selling Process is used for showing which product sold.
 * This data class representing the selling_process database table
 *
 * @property id Selling Process's unique primary key.
 * @property quantity Product's quantity in the cart.
 * @property priceSell Product's net price.
 * @property amount quantity times priceSell.
 * @property sellingFormat Selling Format. Only has 2 types = **Sale**, **Return**
 * @property zId Report Z id. This is a foreign key referencing the report_z table
 * @property xId Report X id. This is a foreign key referencing the report_x table
 * @property userId User id that is selling this product. This is a foreign key referencing the user table
 * @property sellingProcessTypeId Selling Process Type id. Only has 3 types = **Card**, **Cash**, **Other**. This is a foreign key referencing the selling_process_type table.
 * @property productId Product's id. This is a foreign key referencing the product table
 */
data class SellingProcess(
    val id: Int,
    val quantity: Int,
    val priceSell: Double,
    val amount: Double = quantity * priceSell,
    val sellingFormat: String,
    val zId: Int,
    val xId: Int,
    val userId: Int,
    val sellingProcessTypeId: Int,
    val productId: Int
)