package me.yavuz.delta_a_project.model

data class User(
    val id: Int,
    val name: String,
    val password: String,
    val userTypeName: String
) {
    override fun toString(): String {
        return "Id: $id Name: $name Role: $userTypeName"
    }
}