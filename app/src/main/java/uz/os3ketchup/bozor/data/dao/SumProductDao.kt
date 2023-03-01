package uz.os3ketchup.bozor.data.dao

import androidx.room.*
import io.reactivex.rxjava3.core.Flowable
import uz.os3ketchup.bozor.data.AmountProduct
import uz.os3ketchup.bozor.data.SumProduct

@Dao
interface SumProductDao {

    @Insert()
    fun addSumProducts(sumProduct: SumProduct)

    @Delete
    fun deleteSumProduct(sumProduct: SumProduct)

    @Update
    fun editSumProduct(sumProduct: SumProduct)

    @Query("select * from sumProduct")
    fun getAllSumProduct(): Flowable<List<SumProduct>>

    @Query("select * from sumProduct")
    fun getAllSumProducts(): List<SumProduct>


}