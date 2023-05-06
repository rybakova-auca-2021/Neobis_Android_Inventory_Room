package com.example.inventory.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Parcelable
import androidx.room.*
import com.example.inventory.database.ProductDao
import kotlinx.parcelize.Parcelize
import java.io.ByteArrayOutputStream

@Entity(tableName = "products")
@TypeConverters(BitMapTypeConverter::class)
@Parcelize
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    var image: String,
    var name: String,
    var price: String,
    var manufacturer: String,
    var quantity: String
) : Parcelable

class BitMapTypeConverter {
    @TypeConverter
    fun fromByteArray(bytes: ByteArray?): Bitmap? {
        return if (bytes == null) {
            null
        } else {
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }
    }
    @TypeConverter
    fun toByteArray(bitmap: Bitmap?): ByteArray? {
        return if (bitmap == null) {
            null
        } else {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.toByteArray()
        }
    }
}

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