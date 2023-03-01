package uz.os3ketchup.bozor.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

@Entity
data class CategoryData(
    @Embedded
    val category: Category,
    @Relation(
        parentColumn = "categoryId", entityColumn = "productId"
    )
    var productList: List<Product>
)
