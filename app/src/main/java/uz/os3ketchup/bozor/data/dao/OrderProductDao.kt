package uz.os3ketchup.bozor.data.dao

import androidx.room.*
import uz.os3ketchup.bozor.data.OrderProduct

@Dao
interface OrderProductDao {
    @Insert
    fun addOrderProduct(orderProduct: OrderProduct)

    @Delete
    fun deleteOrderProduct(){}

    @Query("UPDATE OrderProduct SET price = :newPrice WHERE product = :product")
    fun updateOrderProductPriceByProduct(product:String,newPrice:Double)

    @Query("SELECT * FROM OrderProduct")
    fun getAllOrderProduct():List<OrderProduct>
}