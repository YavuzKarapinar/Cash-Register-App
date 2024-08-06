package me.yavuz.delta_a_project.database.repository

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.yavuz.delta_a_project.database.DbHelper
import me.yavuz.delta_a_project.database.dao.ProductDAO
import me.yavuz.delta_a_project.model.Product

class ProductRepository(context: Context) {
    private val dbHelper = DbHelper.getInstance(context)
    private val productDAO = ProductDAO(dbHelper.writableDatabase)

    suspend fun getProducts(): List<Product> {
        return withContext(Dispatchers.IO) {
            productDAO.getProducts()
        }
    }

    suspend fun getProductById(id: Int): Product? {
        return withContext(Dispatchers.IO) {
            productDAO.getProductById(id)
        }
    }

    fun isProductExists(name: String): Boolean {
        return productDAO.isProductExists(name)
    }

    suspend fun saveProduct(product: Product) {
        return withContext(Dispatchers.IO) {
            productDAO.saveProduct(product)
        }
    }

    @Throws(SQLiteConstraintException::class)
    suspend fun deleteProduct(product: Product) {
        withContext(Dispatchers.IO) {
            productDAO.deleteProduct(product)
        }
    }

    suspend fun updateProduct(product: Product) {
        withContext(Dispatchers.IO) {
            productDAO.updateProduct(product)
        }
    }

    suspend fun getProductByProductNumber(productNumber: Int): Product? {
        return withContext(Dispatchers.IO) {
            productDAO.getProductByProductNumber(productNumber)
        }
    }
}