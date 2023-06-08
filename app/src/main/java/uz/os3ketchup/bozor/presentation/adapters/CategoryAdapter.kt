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
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import uz.os3ketchup.bozor.R
import uz.os3ketchup.bozor.data.Category
import uz.os3ketchup.bozor.data.database.MyDatabase
import uz.os3ketchup.bozor.databinding.ItemPartBinding

class CategoryAdapter(var context: Context, private var list: List<Category>) :
    RecyclerView.Adapter<CategoryAdapter.VH>() {

    lateinit var database: MyDatabase

    inner class VH(private var itemRV: ItemPartBinding) : ViewHolder(itemRV.root) {
        @RequiresApi(Build.VERSION_CODES.Q)
        fun onBind(category: Category) {
            itemRV.tvItemName.text = category.categoryName
            itemRV.root.setOnClickListener {
                Toast.makeText(context, "item clicked", Toast.LENGTH_SHORT).show()
            }
            itemRV.ivItemMore.setOnClickListener {

                val popupMenu = PopupMenu(context, itemRV.ivItemMore)
                val inflater = popupMenu.menuInflater
                inflater.inflate(R.menu.popup_menu, popupMenu.menu)
                popupMenu.setForceShowIcon(true)
                popupMenu.show()

                popupMenu.setOnMenuItemClickListener {

                    val firebaseDatabase = Firebase.database
                    val databaseRef = firebaseDatabase.getReference("part_products")
                    when (it.itemId) {
                        R.id.item_edit -> {
                            database = MyDatabase.getInstance(context)

                            val dialog = Dialog(context)
                            dialog.setContentView(R.layout.dialog_part)
                            val editTextName: EditText = dialog.findViewById(R.id.et_part_name)
                            val btnCancel: Button = dialog.findViewById(R.id.btn_cancel)
                            val btnSave: Button = dialog.findViewById(R.id.btn_save)
                            editTextName.setText(category.categoryName)
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

                                category.categoryName = editTextName.text.toString()

                                if (editTextName.text.isNotEmpty()) {
                                    database.categoryDao().editCategory(category)
                                    databaseRef.child(category.categoryId.toString()).setValue(category)
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
                        R.id.item_delete -> {
                            database = MyDatabase.getInstance(context)
                            database.categoryDao().deleteCategory(category)

                            databaseRef.child(category.categoryId.toString()).removeValue()
                            true
                        }
                        else -> {
                            false
                        }
                    }
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemPartBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }


}