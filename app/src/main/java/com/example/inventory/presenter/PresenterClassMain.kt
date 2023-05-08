package com.example.inventory.presenter

import android.content.Context
import android.util.Log
import com.example.inventory.model.Product
import com.example.inventory.model.ProductDatabase
import com.example.inventory.database.ProductRepository
import kotlinx.coroutines.*

class PresenterClassMain(context: Context) : Presenter.PresenterMain{
    private var view: Presenter.ProductView? = null
    private val repository: ProductRepository
    private val exceptionHandler = CoroutineExceptionHandler{_ , T ->
        Log.e("Test", T.message.toString())
    }
    init {
        val productDao = ProductDatabase.getInstance(context)?.productDao()
        repository = productDao?.let { ProductRepository(it) }!!
    }

    override fun addProduct(product: Product){
        CoroutineScope(Dispatchers.IO).launch {
            repository.insertProduct(product)
        }
    }
    override fun getAllProducts() {
        Log.e("Test", "getAllProducts")
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val product = repository.getAllProducts()
            withContext(Dispatchers.Main) {
                view?.showAllProducts(product)
            }
        }
    }
    override fun deleteProduct(product: Product) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteProduct(product)
        }
    }

    override fun updateProduct(product: Product) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.updateProduct(product)
        }
    }

    override fun searchProductsByName(name: String) {
        val products = mutableListOf<Product>()
        val allProducts = repository.getAllProducts()
        for (product in allProducts) {
            if (product.name.contains(name, ignoreCase = true)) {
                products.add(product)
            }
        }
        view?.showAllProducts(products)
    }

    override fun attachView(view: Presenter.ProductView) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }
}