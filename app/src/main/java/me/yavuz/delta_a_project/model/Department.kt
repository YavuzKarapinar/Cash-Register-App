package me.yavuz.delta_a_project.model

/**
 * Department for group departments.
 * It is representing department table
 *
 * @property id Department's primary unique key
 * @property groupId Group's unique key. This is a foreign key referencing the group table
 * @property name Department's name. Representing table's unique name column
 */
data class Department(
    val id: Int,
    val groupId: Int,
    val name: String
)
