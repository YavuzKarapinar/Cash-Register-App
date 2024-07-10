package me.yavuz.delta_a_project.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import me.yavuz.delta_a_project.model.Department
import me.yavuz.delta_a_project.model.Group
import me.yavuz.delta_a_project.model.Product
import me.yavuz.delta_a_project.model.Tax

class DbHelper private constructor(context: Context) :
    SQLiteOpenHelper(
        context,
        DatabaseConstants.DATABASE_NAME,
        null,
        DatabaseConstants.DATABASE_VERSION
    ) {

    companion object {
        @Volatile
        private var instance: DbHelper? = null

        fun getInstance(context: Context): DbHelper {
            return instance ?: synchronized(this) {
                instance ?: DbHelper(context.applicationContext).also { instance = it }
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //master data
        db?.execSQL(DatabaseConstants.CREATE_GROUP_TABLE_QUERY)
        db?.execSQL(DatabaseConstants.CREATE_DEPARTMENT_TABLE_QUERY)
        db?.execSQL(DatabaseConstants.CREATE_PRODUCTS_TABLE_QUERY)
        db?.execSQL(DatabaseConstants.CREATE_TAXES_TABLE_QUERY)

        //staff
        db?.execSQL(DatabaseConstants.CREATE_USERS_TABLE_QUERY)
        db?.execSQL(DatabaseConstants.CREATE_USER_TYPE_TABLE_QUERY)

        //selling process
        db?.execSQL(DatabaseConstants.CREATE_SELLING_PROCESS_TABLE_QUERY)
        db?.execSQL(DatabaseConstants.CREATE_SELLING_PROCESS_TYPE_TABLE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
         db?.execSQL("DROP TABLE IF EXISTS `group`")
         db?.execSQL("DROP TABLE IF EXISTS `department`")
         db?.execSQL("DROP TABLE IF EXISTS `products`")
         db?.execSQL("DROP TABLE IF EXISTS `taxes`")
         db?.execSQL("DROP TABLE IF EXISTS `users`")
         db?.execSQL("DROP TABLE IF EXISTS `user_type`")
         db?.execSQL("DROP TABLE IF EXISTS `selling_process`")
         db?.execSQL("DROP TABLE IF EXISTS `selling_process_type`")
         onCreate(db)
    }

    override fun onConfigure(db: SQLiteDatabase?) {
        super.onConfigure(db)
        db?.setForeignKeyConstraintsEnabled(true)
    }

    fun saveGroup(groupName: String) {
        val db = this.readableDatabase
        val values = ContentValues().apply {
            put("name", groupName)
        }

        db.insert("`group`", null, values)
    }

    fun getGroups(): List<Group> {
        val groups = mutableListOf<Group>()
        val sql =
            "SELECT id, name FROM `group`"

        val db = this.readableDatabase

        db.rawQuery(sql, null).use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndexOrThrow("id"))
                val name = it.getString(it.getColumnIndexOrThrow("name"))
                groups.add(Group(id, name))
            }
        }

        return groups
    }

    fun getGroupById(id: Int): Group? {
        val db = this.readableDatabase
        val sql = "SELECT id, name FROM `group` WHERE id = ?"

        db.rawQuery(sql, arrayOf(id.toString())).use {
            if (it.moveToFirst()) {
                return Group(it.getInt(0), it.getString(1))
            }
        }

        return null
    }

    fun getGroupByName(name: String): Group? {
        val db = this.readableDatabase
        val sql = "SELECT id, name FROM `group` WHERE name = ?"

        db.rawQuery(sql, arrayOf(name)).use {
            if (it.moveToFirst()) {
                return Group(it.getInt(0), it.getString(1))
            }
        }

        return null
    }

    fun saveDepartment(group: String, name: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("group_id", getGroupByName(group)?.id)
            put("name", name)
        }

        db.insert("department", null, values)
    }

    fun getDepartmentByName(name: String): Department? {
        val db = this.readableDatabase
        val sql = "SELECT id, group_id, name FROM `department` WHERE name = ?"

        db.rawQuery(sql, arrayOf(name)).use {
            if (it.moveToFirst()) {
                return Department(it.getInt(0), it.getInt(1), it.getString(2))
            }
        }

        return null
    }

    fun getDepartments(): List<Department> {
        val departments = mutableListOf<Department>()
        val db = this.readableDatabase
        val sql = "SELECT id, group_id, name FROM `department`"
        db.rawQuery(sql, null).use {
            while (it.moveToNext()) {
                departments.add(Department(it.getInt(0), it.getInt(1), it.getString(2)))
            }
        }

        return departments
    }

    fun saveTax(name: String, value: Double) {
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put("name", name)
            put("value", value)
        }

        db.insert("taxes", null, values)
    }

    fun getTaxByName(name: String): Tax? {
        val db = this.readableDatabase
        val sql = "SELECT id, name, value FROM taxes WHERE name = ?"

        db.rawQuery(sql, arrayOf(name)).use {
            if (it.moveToFirst()) {
                return Tax(it.getInt(0), it.getString(1), it.getDouble(2))
            }
        }

        return null
    }

    fun getTaxes(): List<Tax> {
        val taxes = mutableListOf<Tax>()
        val db = this.readableDatabase
        val sql = "SELECT id, name, value FROM taxes"
        db.rawQuery(sql, null).use {
            while (it.moveToNext()) {
                taxes.add(Tax(it.getInt(0), it.getString(1), it.getDouble(2)))
            }
        }

        return taxes
    }

    fun saveProduct(product: Product) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("name", product.name)
            put("gross_price", product.price)
            put("stock", product.stock)
            put("tax_id", product.taxId)
            put("department_id", product.departmentId)
            put("product_number", product.productNumber)
        }

        val l = db.insert("products", null, values)
        Log.d("TAG", l.toString())
    }

    fun getProducts(): List<Product> {
        val products = mutableListOf<Product>()
        val db = this.readableDatabase
        val sql =
            "SELECT id, name, gross_price, stock, tax_id, department_id, product_number FROM products"

        db.rawQuery(sql, null).use {
            while (it.moveToNext()) {
                val id = it.getInt(0)
                val name = it.getString(1)
                val grossPrice = it.getDouble(2)
                val stock = it.getInt(3)
                val taxId = it.getInt(4)
                val departmentId = it.getInt(5)
                val productNumber = it.getInt(6)
                products.add(
                    Product(
                        id,
                        name,
                        grossPrice,
                        stock,
                        productNumber,
                        taxId,
                        departmentId
                    )
                )
            }
        }

        return products
    }

    fun deleteProduct(product: Product) {
        val db = this.writableDatabase
        db.delete("products", "id = ?", arrayOf(product.id.toString()))
    }

    fun getProductById(value: Int): Product? {
        val db = this.readableDatabase
        val sql =
            "SELECT id, name, gross_price, stock, tax_id, department_id, product_number FROM products WHERE id = ?"
        db.rawQuery(sql, arrayOf(value.toString())).use {
            if(it.moveToFirst()) {
                val id = it.getInt(0)
                val name = it.getString(1)
                val grossPrice = it.getDouble(2)
                val stock = it.getInt(3)
                val taxId = it.getInt(4)
                val departmentId = it.getInt(5)
                val productNumber = it.getInt(6)

                return Product(id, name, grossPrice, stock, productNumber, taxId, departmentId)
            }
        }

        return null
    }

    fun updateProduct(product: Product) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("name", product.name)
            put("gross_price", product.price)
            put("stock", product.stock)
            put("tax_id", product.taxId)
            put("department_id", product.departmentId)
            put("product_number", product.productNumber)
        }
        db.update("products", values, "id = ?", arrayOf(product.id.toString()))
    }
}