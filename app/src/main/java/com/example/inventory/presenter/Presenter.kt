package com.example.inventory.presenter

import androidx.lifecycle.LiveData
import com.example.inventory.model.Product

interface Presenter {

    interface ProductView {
        fun showAllProducts(products: List<Product>)
    }
    interface ArchivedView : ProductView {
        fun showAllArchived(products: List<Product>)
    }

    interface PresenterMain {
        fun addProduct(product: Product)
        fun deleteProduct(product: Product)
        fun getAllProducts()
        fun updateProduct(product: Product)
        fun attachView(view: ProductView)
        fun detachView()
    }

    interface PresenterArchive {
        fun deleteProduct(product: Product)
        fun getAllArchived()
        fun updateProduct(product: Product)
        fun attachView(view: ArchivedView)
    }
}
