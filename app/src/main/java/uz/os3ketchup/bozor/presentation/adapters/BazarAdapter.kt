package uz.os3ketchup.bozor.presentation.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import uz.os3ketchup.bozor.data.AmountList
import uz.os3ketchup.bozor.data.AmountProduct
import uz.os3ketchup.bozor.data.SumProduct
import uz.os3ketchup.bozor.data.database.MyDatabase
import uz.os3ketchup.bozor.databinding.ItemListBinding

class BazarAdapter(
    var context: Context,
    private var list: List<AmountProduct>, private var sumList: List<SumProduct>
) :
    RecyclerView.Adapter<BazarAdapter.VH>() {

    var myDatabase = MyDatabase.getInstance(context)
    var amountList = ArrayList<AmountProduct>()
    inner class VH(private var itemRV: ItemListBinding) : ViewHolder(itemRV.root) {

        @SuppressLint("SetTextI18n", "CheckResult")
        fun onBind(amountProduct: AmountProduct, position: Int) {

            if (sumList[0].isSelected) {
                itemRV.checkbox.visibility = View.VISIBLE
            } else {
                itemRV.checkbox.visibility = View.INVISIBLE
            }

            itemRV.etPrice.setText(amountProduct.priceProduct.toString())
            itemRV.tvProductName.text = amountProduct.productName
            itemRV.tvAmount.text = "${amountProduct.amountProduct} ${amountProduct.unitProduct}"

            itemRV.etPrice.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                @RequiresApi(Build.VERSION_CODES.N)
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    myDatabase.sumProductDao().getAllSumProducts().forEach { sumProduct ->
                        amountList.addAll(sumProduct.productList)
                    }
                    amountList[position].priceProduct = s.toString().toDouble()


                }

                override fun afterTextChanged(s: Editable?) {


                }
            })

            itemRV.root.setOnLongClickListener {
                myDatabase.sumProductDao().getAllSumProducts().forEach { sumProduct ->
                    myDatabase.sumProductDao().editSumProduct(sumProduct.copy(isSelected = true))
                }
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(list[position], position)
    }


}