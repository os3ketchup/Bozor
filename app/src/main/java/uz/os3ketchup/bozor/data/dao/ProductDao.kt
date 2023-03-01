package uz.os3ketchup.bozor.data.dao

import androidx.room.*
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import uz.os3ketchup.bozor.data.Category
import uz.os3ketchup.bozor.data.Product

@Dao
interface ProductDao {
    @Query("select * from product")
    fun getAllProducts():List<Product>

    @Query("select * from product")
    fun getAllProduct(): Flowable<List<Product>>

    @Query("select * from product")
    fun getProduct():Product


    @Query("select * from product where categoryId = :categoryId")
    fun getProductById(categoryId: Int):List<Product>



    @Update
    fun editProduct(product: Product)

    @Query("select * from product")
    fun deleteAllWords(): List<Product>

    @Insert
    fun addProduct(product: Product)

    @Delete
    fun deleteWord(product: Product)

}