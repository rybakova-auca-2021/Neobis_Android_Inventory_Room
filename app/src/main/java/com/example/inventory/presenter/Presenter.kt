package com.example.inventory.presenter

import com.example.inventory.model.Product

interface Presenter {

    interface ProductView {
        fun showAllProducts(products: List<Product>)
    }

    interface PresenterTwo {
        fun addProduct(product: Product)
        fun deleteProduct(product: Product)
        fun getAllProducts()
        fun updateProduct(product: Product)
        fun attachView(view: ProductView)
        fun detachView()
    }
}
