package uz.os3ketchup.bozor.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity
data class SumProduct(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val date: String,
    var isPassed:Boolean = false,
    var productList: List<OrderProduct>
)

class ProductsTypeConverter {
    @TypeConverter
    fun fromString(value: String?): List<OrderProduct> {
        val listType = object : TypeToken<List<OrderProduct>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(productList: List<OrderProduct>): String {
        return Gson().toJson(productList)
    }
}
