package uz.os3ketchup.bozor.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.os3ketchup.bozor.data.AmountProduct

class BazarViewModel : ViewModel() {
    val totalProduct: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

}