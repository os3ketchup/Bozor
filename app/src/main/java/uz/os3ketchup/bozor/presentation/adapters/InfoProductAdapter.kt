package uz.os3ketchup.bozor.presentation.adapters

import android.content.Context
import android.icu.math.BigDecimal
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import uz.os3ketchup.bozor.R
import uz.os3ketchup.bozor.data.ProductInfo
import uz.os3ketchup.bozor.databinding.ItemProductInfoBinding
import java.math.RoundingMode
import kotlin.jvm.internal.Intrinsics.Kotlin
import kotlin.math.ceil
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class InfoProductAdapter(var context: Context, var list: List<ProductInfo>) :
    RecyclerView.Adapter<InfoProductAdapter.VH>() {

    inner class VH(private var itemRV: ItemProductInfoBinding) : ViewHolder(itemRV.root) {
        fun onBind(productInfo: ProductInfo, position: Int) {
            val divisionResult: Double
            if (position > 0) {
                divisionResult =
                    (list[position].productPrice / list[position - 1].productPrice * 100) - 100
                if (divisionResult>0){
                    itemRV.ivProgress.setImageResource(R.drawable.increase)
                }else if (divisionResult<0){
                    itemRV.ivProgress.setImageResource(R.drawable.decrease)
                }else{
                    itemRV.ivProgress.setImageResource(R.drawable.ic_stable)
                }
                val result = "%.2f".format(divisionResult)
                itemRV.tvPercentage.text = result
                // use the division result here
            } else {
                // handle the case where position+1 is not available or null

            }





            itemRV.tvAmountOfProduct.text = productInfo.productAmount.toString()
            itemRV.tvPriceProduct.text = productInfo.productPrice.toString()
            itemRV.tvProductDate.text = productInfo.priceDate


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            ItemProductInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(list[position], position)
    }
}