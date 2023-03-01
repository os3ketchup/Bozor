package uz.os3ketchup.bozor.data.database

import android.content.Context
import androidx.room.*
import uz.os3ketchup.bozor.data.*
import uz.os3ketchup.bozor.data.dao.*


@Database(
    entities = [Category::class, Product::class, AmountProduct::class, SumProduct::class, OrderProduct::class],
    version = 1
)
@TypeConverters(ProductsTypeConverter::class)
abstract class MyDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun productDao(): ProductDao
    abstract fun amountProductDao(): AmountProductDao
    abstract fun sumProductDao(): SumProductDao
    abstract fun orderProductDao(): OrderProductDao

    companion object {
        private var instance: MyDatabase? = null

        @Synchronized
        fun getInstance(context: Context): MyDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context, MyDatabase::class.java, "productDb")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }
}