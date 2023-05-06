package com.example.inventory.presenter

import android.content.Context
import com.example.inventory.model.Product
import com.example.inventory.model.ProductDatabase
import com.example.inventory.database.ProductRepository
import kotlinx.coroutines.*

class PresenterClassArchived(context: Context) : Presenter.PresenterArchive{
    private var viewArchived: Presenter.ArchivedView? = null
    private val repository: ProductRepository
    init {
        val productDao = ProductDatabase.getInstance(context)?.productDao()
        repository = productDao?.let { ProductRepository(it) }!!
    }

    override fun getAllArchived() {
        CoroutineScope(Dispatchers.IO).launch {
            val product = repository.getAllArchived()
            withContext(Dispatchers.Main) {
                viewArchived?.showAllArchived(product)
            }
        }
    }

    override fun deleteProduct(product: Product) {
        GlobalScope.launch(Dispatchers.IO) {
            repository.deleteProduct(product)
        }
    }

    override fun updateProduct(product: Product) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.updateProduct(product)
        }
    }

    override fun attachView(view: Presenter.ArchivedView) {
        this.viewArchived = view
    }
}