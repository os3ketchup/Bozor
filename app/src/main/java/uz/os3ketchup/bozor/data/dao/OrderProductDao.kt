package uz.os3ketchup.bozor.data.dao

import androidx.room.*
import io.reactivex.rxjava3.core.Flowable
import uz.os3ketchup.bozor.data.OrderProduct

@Dao
interface OrderProductDao {
    @Insert
    fun addOrderProduct(orderProduct: OrderProduct)

    @Delete
    fun deleteOrderProduct(orderProduct: OrderProduct)

    @Query("UPDATE OrderProduct SET price = :newPrice WHERE product = :product")
    fun updateOrderProductPriceByProduct(product:String,newPrice:Double)



    @Query("SELECT * FROM OrderProduct")
    fun getAllOrderProduct():List<OrderProduct>

    @Query("SELECT * FROM OrderProduct")
    fun getAllOrderProducts():Flowable<List<OrderProduct>>

    @Update
    fun editOrderProduct(orderProduct: OrderProduct)
}