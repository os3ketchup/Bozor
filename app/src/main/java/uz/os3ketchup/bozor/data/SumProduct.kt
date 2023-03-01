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
    val sumProduct: Double,
    var isSelected:Boolean = false,
    var productList: List<AmountProduct>
)

class ProductsTypeConverter {
    @TypeConverter
    fun fromString(value: String?): List<AmountProduct> {
        val listType = object : TypeToken<List<AmountProduct>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(productList: List<AmountProduct>): String {
        return Gson().toJson(productList)
    }
}
