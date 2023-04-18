package uz.os3ketchup.bozor.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import uz.os3ketchup.bozor.R
import uz.os3ketchup.bozor.data.SumProduct
import uz.os3ketchup.bozor.databinding.FragmentDateProductsBinding
import uz.os3ketchup.bozor.databinding.ItemOrdersBinding

class DateAdapter(
    var context: Context,
    private var list: List<SumProduct>,
    var navController: NavController
) :
    RecyclerView.Adapter<DateAdapter.VH>() {

    inner class VH(private var itemRV: ItemOrdersBinding) : ViewHolder(itemRV.root) {
        fun onBind(sumProduct: SumProduct) {
            itemRV.itemTvYear.text = sumProduct.date
            itemRV.root.setOnClickListener {

                navController.navigate(R.id.aboutListFragment)


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemOrdersBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(list[position])
    }
}