package com.example.inventory

import android.content.Context
import android.os.Parcelable
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.inventory.database.ProductDao
import com.example.inventory.fragments.MainFragment
import kotlinx.parcelize.Parcelize

@Entity(tableName = "products")
@Parcelize
data class Product(

    @PrimaryKey(autoGenerate = true)
    var image: String,
    val name: String,
    val price: String,
    val manufacturer: String,
    val quantity: String,
) : Parcelable

@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao

    companion object {
        private const val DATABASE_NAME = "product_database"

        @Volatile
        private var instance: ProductDatabase? = null

        fun getInstance(context: Context): ProductDatabase? {
            if (instance == null) {
                synchronized(ProductDatabase::class) {
                    instance = Room.databaseBuilder(context.applicationContext,
                        ProductDatabase::class.java, "user.db").allowMainThreadQueries()
                        .build()
                }
            }
            return instance
        }

        fun destroyInstance() {
            instance = null
        }
    }
}