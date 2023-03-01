package uz.os3ketchup.bozor.presentation.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import uz.os3ketchup.bozor.data.AmountProduct
import uz.os3ketchup.bozor.databinding.ItemProductBinding

class AmountProductAdapter(var context: Context, private var list: List<AmountProduct>) :
    RecyclerView.Adapter<AmountProductAdapter.VH>() {


    inner class VH(private var itemRV: ItemProductBinding) : ViewHolder(itemRV.root) {
        @RequiresApi(Build.VERSION_CODES.Q)
        fun onBind(amountProduct: AmountProduct) {


            itemRV.tvProductName.text = amountProduct.productName
            itemRV.tvAmountOfProduct.text = amountProduct.amountProduct
            itemRV.tvUnitOfProduct.text = amountProduct.unitProduct
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }


}