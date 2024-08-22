package me.yavuz.delta_a_project.model

/**
 * User data class that is used for users that is using this application.
 * This data class representing user database table.
 *
 * @property id User's unique primary key.
 * @property name User's unique name.
 * @property password User's password.
 * @property userTypeName User's user type. This is a foreign key referencing the user_type table.
 * User type can only be 2 types. **Admin**, **Staff**.
 */
data class User(
    val id: Int,
    val name: String,
    val password: String,
    val userTypeName: String
) {
    /**
     * This is as override toString() method that is showing only id, name and role and not password.
     */
    override fun toString(): String {
        return "Id: $id Name: $name Role: $userTypeName"
    }
}