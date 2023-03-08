package uz.os3ketchup.bozor.data.dao

import androidx.room.*
import io.reactivex.rxjava3.core.Flowable
import uz.os3ketchup.bozor.data.AmountProduct

@Dao
interface AmountProductDao {

    @Insert
    fun addAmountProduct(amountProduct: AmountProduct)

    @Insert
    fun addAmountProduct(list: List<AmountProduct>)

    @Delete
    fun deleteAmountCategory(amountProduct: AmountProduct)

    @Update
    fun editAmountCategory(amountProduct: AmountProduct)

    @Query("select * from amountProduct")
    fun getAllAmountProduct(): Flowable<List<AmountProduct>>

    @Query("select * from amountProduct")
    fun getAllAmountProducts(): List<AmountProduct>

    @Query("DELETE FROM AmountProduct")
    fun clearTable()


}