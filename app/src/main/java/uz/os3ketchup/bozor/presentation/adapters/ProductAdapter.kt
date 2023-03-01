package uz.os3ketchup.bozor.presentation.adapters

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import uz.os3ketchup.bozor.R
import uz.os3ketchup.bozor.data.Product
import uz.os3ketchup.bozor.data.database.MyDatabase
import uz.os3ketchup.bozor.databinding.ItemListProductBinding
import uz.os3ketchup.bozor.databinding.ItemParticularProductsBinding
import uz.os3ketchup.bozor.databinding.ItemProductBinding

class ProductAdapter(var context: Context, private var list: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.VH>() {
    inner class VH(private var itemRV: ItemParticularProductsBinding) : ViewHolder(itemRV.root) {

        val database = MyDatabase.getInstance(context)
        @RequiresApi(Build.VERSION_CODES.Q)
        fun onBind(product: Product) {
            if (database.productDao().getAllProducts().isNotEmpty()) {
                database.categoryDao().getAllCategory().forEach {
                    if (it.categoryId == product.categoryId) {
                        itemRV.tvCategoryName.text = it.categoryName
                    }
                }
                itemRV.tvProductName.text = product.productName
            }
            itemRV.ivItemMore.setOnClickListener {
                val popupMenu = PopupMenu(context,itemRV.ivItemMore)
                val inflater = popupMenu.menuInflater
                inflater.inflate(R.menu.popup_menu,popupMenu.menu)
                popupMenu.setForceShowIcon(true)
                popupMenu.show()


                popupMenu.setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.item_edit->{

                            val dialog = Dialog(context)
                            dialog.setContentView(R.layout.dialog_part)
                            val editTextName: EditText = dialog.findViewById(R.id.et_part_name)
                            val btnCancel: Button = dialog.findViewById(R.id.btn_cancel)
                            val btnSave: Button = dialog.findViewById(R.id.btn_save)
                            editTextName.setText(product.productName)
                            dialog.setCancelable(false)
                            dialog.show()
                            dialog.window?.setLayout(
                                ActionBar.LayoutParams.MATCH_PARENT,
                                ActionBar.LayoutParams.WRAP_CONTENT
                            )
                            btnCancel.setOnClickListener {
                                dialog.cancel()
                            }
                            btnSave.setOnClickListener {

                                product.productName = editTextName.text.toString()

                                if (editTextName.text.isNotEmpty()) {
                                    database.productDao().editProduct(product)
                                    Toast.makeText(context, "saved", Toast.LENGTH_SHORT)
                                        .show()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "please fill the gaps",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                                dialog.cancel()
                            }

                            true
                        }
                        R.id.item_delete->{
                            database.productDao().deleteWord(product)
                            true
                        }
                        else->{
                            false
                        }
                    }
                }

            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            ItemParticularProductsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(list[position])
    }
}