package me.yavuz.delta_a_project.model

/**
 * It is representing tax table
 *
 * @property id Tax's primary unique key
 * @property name Tax's name. Representing table's unique name column
 * @property value Tax's value. It can be greater than 0.0 and less than 100.0.
 */
data class Tax(
    val id: Int,
    val name: String,
    val value: Double
)
