package uz.os3ketchup.bozor.data

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class OrderProduct(
    @PrimaryKey(autoGenerate = true)
    val id:Int? = null,
    val UID:String = "",
    val category:String,
    val product:String,
    val price:Double,
    val amount:Double,
    val date:String,
    val unit:String,
    val sum:Double,
    val isLongClicked:Boolean = false,
    var isChecked:Boolean = false,
)
