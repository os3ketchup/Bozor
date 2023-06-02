package uz.os3ketchup.bozor.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import uz.os3ketchup.bozor.data.ProductInfo

@Dao
interface ProductInfoDao {

    @Insert
    fun addProductInfo(productInfo: ProductInfo)

    @Delete
    fun deleteProductInfo(productInfo: ProductInfo)

    @Query("SELECT * FROM ProductInfo")
    fun getAllProductInfo():List<ProductInfo>

    @Query("DELETE FROM ProductInfo")
    fun clearTable()
}