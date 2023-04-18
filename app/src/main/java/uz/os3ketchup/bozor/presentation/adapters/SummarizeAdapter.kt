package uz.os3ketchup.bozor.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import uz.os3ketchup.bozor.R
import uz.os3ketchup.bozor.data.SumProduct
import uz.os3ketchup.bozor.databinding.ItemListProductBinding
import uz.os3ketchup.bozor.databinding.ItemOrdersBinding

class SummarizeAdapter(
    var context: Context,
    private var list: List<SumProduct>
) :
    RecyclerView.Adapter<SummarizeAdapter.VH>() {



    inner class VH(private var itemRV: ItemListProductBinding) : ViewHolder(itemRV.root) {
        fun onBind(sumProduct: SumProduct) {
            itemRV.tvProductName.text = sumProduct.productList[0].product
            itemRV.tvAmount.text = sumProduct.productList[0].amount.toString()
            itemRV.tvSum.text = sumProduct.productList[0].sum.toString()

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            ItemListProductBinding.inflate(
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