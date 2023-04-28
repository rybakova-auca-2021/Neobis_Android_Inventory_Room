package com.example.inventory.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.inventory.Product

@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class ProductDataBase : RoomDatabase() {

    abstract fun ProductDao(): ProductDao

    companion object {
        private var INSTANCE: ProductDataBase? = null

        fun getInstance(context: Context): ProductDataBase? {
            if (INSTANCE == null) {
                synchronized(ProductDataBase::class) {
                    INSTANCE = Room.databaseBuilder (
                        context.applicationContext,
                        ProductDataBase::class.java,
                        "product.dataBase")
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}