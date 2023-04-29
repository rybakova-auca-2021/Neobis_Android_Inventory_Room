package com.example.inventory.database

import android.content.Context
import android.os.AsyncTask
import com.example.inventory.Product
import com.example.inventory.ProductDatabase

class ProductRepository(context: Context) {
    var db: ProductDao = ProductDatabase.getInstance(context)?.productDao()!!


    //Fetch All the products
    fun getAllProducts(): List<Product> {
        return db.getAllProducts()
    }

    // Insert new product
    fun insertProduct(product: Product) {
        insertAsyncTask(db).execute(product)
    }

    // update product
    fun updateProduct(product: Product) {
        db.updateProduct(product)
    }

    // Delete product
    fun deleteProduct(product: Product) {
        db.deleteProduct(product)
    }

    private class insertAsyncTask internal constructor(private val ProductDao: ProductDao) :
        AsyncTask<Product, Void, Void>() {

        override fun doInBackground(vararg params: Product): Void? {
            ProductDao.insertProduct(params[0])
            return null
        }
    }
}