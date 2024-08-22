package me.yavuz.delta_a_project.model

/**
 * Product group. It is representing Group database table
 *
 * @property id Group's primary unique key
 * @property name Group's name. Representing table's unique name column
 */
data class Group(
    val id: Int,
    val name: String
)
