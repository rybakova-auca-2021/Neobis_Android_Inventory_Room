package com.example.inventory

import android.content.Context
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.inventory.database.ProductDao

@Entity(tableName = "products")
data class Product (

    @PrimaryKey(autoGenerate = true)
    val image: String,
    val name: String,
    val price: Double,
    val manufacturer: String,
    val quantity: Int,
    val archived: Boolean
)

@Database(entities = [Product::class], version = 1)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao

    companion object {
        private const val DATABASE_NAME = "product_database"

        @Volatile
        private var instance: ProductDatabase? = null

        fun getInstance(context: Context): ProductDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): ProductDatabase {
            return Room.databaseBuilder(context, ProductDatabase::class.java, DATABASE_NAME)
                .build()
        }
    }
}