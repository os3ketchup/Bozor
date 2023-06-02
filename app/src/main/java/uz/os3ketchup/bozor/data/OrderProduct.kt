package uz.os3ketchup.bozor.data

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.IgnoreExtraProperties

@Entity
@Keep
data class OrderProduct(
    var amount:Double = 0.0,
    var category:String = "",
    var isChecked:Boolean = false,
    var date:String = "",
    @PrimaryKey(autoGenerate = true)
    var id:Int? = null,
    var isLongClicked:Boolean = false,
    var price:Double = 0.0,
    var product:String = "",
    var sum:Double = 0.0,
    var UID:String = "",
    var unit:String = "",
)
