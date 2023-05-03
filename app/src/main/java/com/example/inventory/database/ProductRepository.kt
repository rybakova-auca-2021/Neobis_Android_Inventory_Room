package com.example.inventory.database

import com.example.inventory.model.Product

class ProductRepository(private val ProductDao: ProductDao) {

    //Fetch All the products
    fun getAllProducts(): List<Product> {
        return ProductDao.getAllProducts()
    }

    // Insert new product
    fun insertProduct(product: Product) {
        return ProductDao.insertProduct(product)
    }

    // update product
    fun updateProduct(product: Product) {
        return ProductDao.updateProduct(product)
    }

    // Delete product
    fun deleteProduct(product: Product) {
        return ProductDao.deleteProduct(product)
    }
}