package uz.os3ketchup.bozor.data.dao

import androidx.room.*
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import uz.os3ketchup.bozor.data.Category
import uz.os3ketchup.bozor.data.CategoryData
import uz.os3ketchup.bozor.data.Product

@Dao
interface CategoryDao {
    @Transaction
    @Query("select * from category")
    fun getCategoryByProduct(): List<CategoryData>

    @Query("select * from category")
    fun getAllCategories(): Flowable<List<Category>>

    @Query("select * from category")
    fun getAllCategory(): List<Category>

    @Insert
    fun addCategory(category: Category)


    @Insert
    fun addAllCategory(vararg category: Category)

    @Insert
    fun addListCategory(list: List<Category>)

    @Delete
    fun deleteCategory(category: Category)

    @Update
    fun editCategory(category: Category)
}