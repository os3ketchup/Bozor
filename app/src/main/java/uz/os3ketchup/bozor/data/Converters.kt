package uz.os3ketchup.bozor.data

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson


@ProvidedTypeConverter
class Converters {


    @TypeConverter

    fun arrayListToJson(amountList: AmountList): String {
        val gson = Gson()
        return gson.toJson(amountList)
    }

    @TypeConverter
    fun jsonToArrayList(jsonData: String?): AmountList {
        val gson = Gson()
        return gson.fromJson(jsonData,AmountList::class.java)
    }
}