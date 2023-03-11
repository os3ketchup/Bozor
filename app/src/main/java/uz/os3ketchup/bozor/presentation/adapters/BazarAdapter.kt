package uz.os3ketchup.bozor.presentation.adapters

import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import uz.os3ketchup.bozor.R
import uz.os3ketchup.bozor.data.AmountList
import uz.os3ketchup.bozor.data.AmountProduct
import uz.os3ketchup.bozor.data.OrderProduct
import uz.os3ketchup.bozor.data.SumProduct
import uz.os3ketchup.bozor.data.database.MyDatabase
import uz.os3ketchup.bozor.databinding.ItemListBinding

class BazarAdapter(
    var context: Context,
    private var list: List<OrderProduct>
) :
    RecyclerView.Adapter<BazarAdapter.VH>() {

    var myDatabase = MyDatabase.getInstance(context)


    //    var amountList = ArrayList<AmountProduct>()
    inner class VH(private var itemRV: ItemListBinding) : ViewHolder(itemRV.root) {

        @SuppressLint("SetTextI18n", "CheckResult")
        fun onBind(orderProduct: OrderProduct) {

            itemRV.etPrice.text = orderProduct.price.toString()
            itemRV.tvProductName.text = orderProduct.product
            itemRV.tvAmount.text = "${orderProduct.amount} ${orderProduct.unit}"
            itemRV.checkbox.isChecked = orderProduct.isChecked
            itemRV.checkbox.setOnCheckedChangeListener { _, isChecked ->
                orderProduct.isChecked = isChecked
                myDatabase.orderProductDao().editOrderProduct(orderProduct)
            }


            itemRV.ivIcEdit.setOnClickListener {
                val dialog = Dialog(context)
                dialog.setContentView(R.layout.dialog_bazar_edit)
                dialog.window?.setLayout(
                    ActionBar.LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT
                )
//            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


                val labelProduct = dialog.findViewById<TextView>(R.id.label_product)
                val priceItem = dialog.findViewById<EditText>(R.id.et_price_item)
                val amountItem = dialog.findViewById<EditText>(R.id.et_amount_item)
                val buttonCancel = dialog.findViewById<Button>(R.id.btn_cancel)
                val buttonAccept = dialog.findViewById<Button>(R.id.btn_accept)
                labelProduct.text = orderProduct.product
                priceItem.setText(orderProduct.price.toString())
                amountItem.setText(orderProduct.amount.toString())
                buttonAccept.setOnClickListener {
                    val amounts = amountItem.text.toString().toDouble()
                    val price = priceItem.text.toString().toDouble()
                    myDatabase.orderProductDao()
                        .updateOrderProductPriceByProduct(orderProduct.product, newPrice = price)
                    myDatabase.orderProductDao().getAllOrderProduct().forEach {
                        myDatabase.orderProductDao()
                            .editOrderProduct(
                                it.copy(
                                    sum = it.amount * it.price,
                                    isLongClicked = it.isLongClicked
                                )
                            )
                    }



                    dialog.cancel()
                }
                buttonCancel.setOnClickListener { dialog.dismiss() }
                dialog.show()

            }

/*
            itemRV.etPrice.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


                }

                override fun afterTextChanged(s: Editable?) {
                    myDatabase.orderProductDao().updateOrderProductPriceByProduct(
                        product = orderProduct.product,
                        newPrice = s.toString().toDouble()
                    )

                    myDatabase.orderProductDao().getAllOrderProduct().forEach {
                        myDatabase.orderProductDao()
                            .editOrderProduct(it.copy(sum = it.amount * it.price))
                    }


                }
            })
*/

            itemRV.root.setOnLongClickListener {
                myDatabase.orderProductDao().getAllOrderProduct().forEach {
                    myDatabase.orderProductDao().editOrderProduct(it.copy(isLongClicked = true))
                }
                true
            }


            if (list[0].isLongClicked) {
                itemRV.checkbox.visibility = View.VISIBLE
                itemRV.ivIcEdit.visibility = View.INVISIBLE

            } else {
                itemRV.checkbox.visibility = View.INVISIBLE
                itemRV.ivIcEdit.visibility = View.VISIBLE
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
        holder.onBind(list[position])
    }


}