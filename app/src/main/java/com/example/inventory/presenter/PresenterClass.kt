package com.example.inventory.presenter

import android.content.Context
import com.example.inventory.model.Product
import com.example.inventory.model.ProductDatabase
import com.example.inventory.database.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PresenterClass(context: Context) : Presenter.PresenterTwo{
    private var view: Presenter.ProductView? = null
    private val repository: ProductRepository
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
        CoroutineScope(Dispatchers.IO).launch {
            val product = repository.getAllProducts()
            withContext(Dispatchers.Main) {
                view?.showAllProducts(product)
            }
        }
    }
    override fun deleteProduct(product: Product) {
        return repository.deleteProduct(product)
    }

    override fun updateProduct(product: Product) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.updateProduct(product)
        }
    }

    override fun attachView(view: Presenter.ProductView) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }
}