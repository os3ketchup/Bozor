package uz.os3ketchup.bozor.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = ["categoryId"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class Product(
    @PrimaryKey(autoGenerate = true)
    val productId: Int? = null,
    var productName: String = "",
    var productAmount: Double = 0.0,
    val productUnit: String = "",
    val categoryId: Int?  = null
)
