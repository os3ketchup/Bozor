package uz.os3ketchup.bozor.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import uz.os3ketchup.bozor.data.ProductInfo
import uz.os3ketchup.bozor.databinding.ItemProductInfoBinding

class InfoProductAdapter(var context: Context, var list: List<ProductInfo>) :
    RecyclerView.Adapter<InfoProductAdapter.VH>() {
    inner class VH(private var itemRV: ItemProductInfoBinding) : ViewHolder(itemRV.root) {
        fun onBind(productInfo: ProductInfo) {
            itemRV.tvAmountOfProduct.text = productInfo.productAmount.toString()
            itemRV.tvPercentage.text = productInfo.productChanging.toString()
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
        holder.onBind(list[position])
    }
}