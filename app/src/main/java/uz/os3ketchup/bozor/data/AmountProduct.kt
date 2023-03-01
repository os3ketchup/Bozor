package uz.os3ketchup.bozor.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AmountProduct(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val productCategory: String = "",
    val productName: String = "",
    val amountProduct: String = "",
    val unitProduct: String = "",
    var priceProduct: Double = 0.0
)
