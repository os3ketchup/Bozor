package uz.os3ketchup.bozor.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category(
    @PrimaryKey(autoGenerate = true)
    val categoryId:Int? = null,
    var categoryName:String
)
