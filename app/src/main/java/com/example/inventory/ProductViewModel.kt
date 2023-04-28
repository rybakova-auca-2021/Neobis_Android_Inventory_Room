package com.example.inventory

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.database.ProductDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {
    private val productDao: ProductDao = ProductDatabase.getInstance(application).productDao()

    private val allProducts: List<Product> = productDao.getAllProducts()

    fun insertProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            productDao.insertProduct(product)
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            productDao.updateProduct(product)
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            productDao.deleteProduct(product)
        }
    }

    fun getAllProducts(): List<Product> {
        return allProducts
    }
}