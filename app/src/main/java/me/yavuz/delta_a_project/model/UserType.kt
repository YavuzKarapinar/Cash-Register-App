package me.yavuz.delta_a_project.model

/**
 * This is user's user type.
 * User type only has 2 types. **Admin**, **Staff**.
 *
 * If user has **admin role** it can be used for anything. It has **every authority**.
 *
 * If user has **staff role** it can be only used for making sales. It has **no authority**.
 *
 * @property id User Type's unique primary key
 * @property name User Type's name
 */
data class UserType(
    val id: Int,
    val name: String
)