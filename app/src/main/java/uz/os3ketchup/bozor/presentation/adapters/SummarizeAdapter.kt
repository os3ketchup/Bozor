package uz.os3ketchup.bozor.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import uz.os3ketchup.bozor.R
import uz.os3ketchup.bozor.data.OrderProduct
import uz.os3ketchup.bozor.data.SumProduct
import uz.os3ketchup.bozor.databinding.ItemListProductBinding
import uz.os3ketchup.bozor.databinding.ItemOrdersBinding

class SummarizeAdapter(
    var context: Context,
    private var list: List<OrderProduct>,var positions: Int
) :
    RecyclerView.Adapter<SummarizeAdapter.VH>() {



    inner class VH(private var itemRV: ItemListProductBinding) : ViewHolder(itemRV.root) {
        fun onBind(orderProduct: OrderProduct) {

            itemRV.tvProductName.text = orderProduct.product
            itemRV.tvAmount.text = orderProduct.amount.toString()
            itemRV.tvSum.text = orderProduct.sum.toString()

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