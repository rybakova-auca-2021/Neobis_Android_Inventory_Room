package com.example.inventory.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.inventory.model.Product

@Dao
interface ProductDao {
    @Query("SELECT * FROM products WHERE archived=0")
    fun getAllProducts(): List<Product>

    @Query("SELECT * FROM products WHERE archived=1")
    fun getAllArchived(): List<Product>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(product: Product)

    @Update
    fun updateProduct(product: Product)

    @Delete
    fun deleteProduct(product: Product)
}
