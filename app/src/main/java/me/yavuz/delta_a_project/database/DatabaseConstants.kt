package me.yavuz.delta_a_project.database

object DatabaseConstants {
    const val DATABASE_NAME = "delta_x"
    const val DATABASE_VERSION = 2

    const val CREATE_GROUP_TABLE_QUERY =
        "CREATE TABLE `groups` (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT UNIQUE" +
                ")"

    const val CREATE_DEPARTMENT_TABLE_QUERY =
        "CREATE TABLE `departments` (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT UNIQUE, " +
                "group_id INTEGER, " +
                "FOREIGN KEY (group_id) REFERENCES `groups`(id)" +
                ")"

    const val CREATE_PRODUCTS_TABLE_QUERY =
        "CREATE TABLE `products` (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT UNIQUE, " +
                "department_id INTEGER, " +
                "product_number INTEGER, " +
                "tax_id INTEGER, " +
                "gross_price REAL, " +
                "stock INTEGER, " +
                "FOREIGN KEY (department_id) REFERENCES `departments`(id), " +
                "FOREIGN KEY (tax_id) REFERENCES `taxes`(id)" +
                ")"

    const val CREATE_TAXES_TABLE_QUERY =
        "CREATE TABLE `taxes` (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT UNIQUE, " +
                "value REAL" +
                ")"

    const val CREATE_USERS_TABLE_QUERY =
        "CREATE TABLE `users` (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_type_id INTEGER, " +
                "name TEXT UNIQUE, " +
                "password TEXT, " +
                "FOREIGN KEY (user_type_id) REFERENCES `user_type`(id)" +
                ")"

    const val CREATE_USER_TYPE_TABLE_QUERY =
        "CREATE TABLE `user_type` (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT" +
                ")"

    const val CREATE_SELLING_PROCESS_TABLE_QUERY =
        "CREATE TABLE `selling_process` (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "selling_process_type_id INTEGER, " +
                "product_id INTEGER, " +
                "quantity INTEGER, " +
                "price_sell REAL, " +
                "amount REAL, " +
                "user_id INTEGER, " +
                "selling_format TEXT," +
                "z_id INTEGER," +
                "FOREIGN KEY (selling_process_type_id) REFERENCES `selling_process_type`(id) ON DELETE RESTRICT, " +
                "FOREIGN KEY (product_id) REFERENCES `products`(id) ON DELETE RESTRICT, " +
                "FOREIGN KEY (user_id) REFERENCES `users`(id) ON DELETE RESTRICT," +
                "FOREIGN KEY (z_id) REFERENCES `report_z`(id) ON DELETE RESTRICT" +
                ")"

    const val CREATE_SELLING_PROCESS_TYPE_TABLE_QUERY =
        "CREATE TABLE `selling_process_type` (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT" +
                ")"

    const val CREATE_REPORT_Z_TABLE_QUERY =
        "CREATE TABLE `report_z` (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "timestamp INTEGER" +
                ")"

    const val CREATE_REPORT_X_TABLE_QUERY =
        "CREATE TABLE `report_x` (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "x_number INTEGER," +
                "z_number INTEGER," +
                "timestamp INTEGER," +
                "FOREIGN KEY (z_number) REFERENCES `report_z`(z_number) ON DELETE RESTRICT" +
                ")"
}