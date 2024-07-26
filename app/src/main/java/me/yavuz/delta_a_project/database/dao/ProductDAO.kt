package me.yavuz.delta_a_project.database.dao

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import me.yavuz.delta_a_project.model.Product

class ProductDAO(private val db: SQLiteDatabase) {

    fun getProducts(): List<Product> {
        val products = mutableListOf<Product>()
        val sql =
            "SELECT id, name, gross_price, stock, product_number, tax_id, department_id FROM products"

        db.rawQuery(sql, null).use {
            while (it.moveToNext()) {
                products.add(
                    Product(
                        it.getInt(0),
                        it.getString(1),
                        it.getDouble(2),
                        it.getInt(3),
                        it.getInt(4),
                        it.getInt(5),
                        it.getInt(6)
                    )
                )
            }
        }

        return products
    }

    fun getProductById(id: Int): Product? {
        val sql =
            "SELECT id, name, gross_price, stock, product_number, tax_id, department_id FROM products WHERE id = ?"
        db.rawQuery(sql, arrayOf(id.toString())).use {
            if (it.moveToFirst()) {
                return Product(
                    it.getInt(0),
                    it.getString(1),
                    it.getDouble(2),
                    it.getInt(3),
                    it.getInt(4),
                    it.getInt(5),
                    it.getInt(6)
                )
            }
        }

        return null
    }

    fun isProductExists(name: String): Boolean {
        val sql =
            "SELECT * FROM products WHERE name = ?"

        db.rawQuery(sql, arrayOf(name)).use {
            return it.moveToFirst()
        }
    }

    fun saveProduct(product: Product) {
        val values = ContentValues().apply {
            put("name", product.name)
            put("gross_price", product.price)
            put("stock", product.stock)
            put("tax_id", product.taxId)
            put("department_id", product.departmentId)
            put("product_number", product.productNumber)
        }
        db.insert("products", null, values)
    }

    fun deleteProduct(product: Product) {
        db.delete("products", "id = ?", arrayOf(product.id.toString()))
    }

    fun updateProduct(product: Product) {
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