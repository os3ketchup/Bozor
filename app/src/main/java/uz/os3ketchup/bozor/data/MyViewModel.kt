package uz.os3ketchup.bozor.data

import androidx.lifecycle.ViewModel

class MyViewModel:ViewModel() {
    private val itemList = mutableListOf<OrderProduct>()

    fun addItem(item:OrderProduct){
        itemList.add(item)
    }

    fun getItemList():List<OrderProduct>{
        return itemList
    }
}