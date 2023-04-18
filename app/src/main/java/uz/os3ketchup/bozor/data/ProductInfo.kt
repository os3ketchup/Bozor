package uz.os3ketchup.bozor.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProductInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val productName: String,
    val priceDate: String,
    val productAmount: Double,
    val productPrice: Double,
    val productChanging: Int = 0
)
