package me.yavuz.delta_a_project.model

/**
 * This data class representing selling_process_type database table.
 * This class only has 3 types.
 *
 * 1) Card: This type used for credit card operations.
 * 2) Cash: This type used for cash operations.
 * 3) Other: This type used for other than card or cash.
 *
 * @property id Selling Process Type's unique primary key.
 * @property name Selling Process Type's name.
 */
data class SellingProcessType(
    val id: Int,
    val name: String
)