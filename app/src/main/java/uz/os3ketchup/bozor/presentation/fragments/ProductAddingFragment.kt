package uz.os3ketchup.bozor.presentation.fragments

import android.app.ActionBar.LayoutParams
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.CalendarContract.Colors
import android.text.Editable
import android.text.Layout
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import uz.os3ketchup.bozor.R
import uz.os3ketchup.bozor.data.Product
import uz.os3ketchup.bozor.data.dao.ProductDao_Impl
import uz.os3ketchup.bozor.data.database.MyDatabase
import uz.os3ketchup.bozor.databinding.FragmentProductAddingBinding
import uz.os3ketchup.bozor.databinding.FragmentProductBinding
import uz.os3ketchup.bozor.presentation.adapters.ProductAdapter


class ProductAddingFragment : Fragment() {
    lateinit var binding: FragmentProductAddingBinding
    private lateinit var myDatabase: MyDatabase
    private lateinit var dialog: Dialog
    private var cat: Int? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProductAddingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myDatabase = MyDatabase.getInstance(requireContext())



        val listCategory = myDatabase.categoryDao().getAllCategory()

        val listCategoryName = ArrayList<String>()

        listCategory.forEach {
            listCategoryName.add(it.categoryName)
        }



        binding.tvCategory.setOnClickListener {
            dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.dialog_particular_product)
            dialog.window?.setLayout(LayoutParams.MATCH_PARENT, 800)
//            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()

            val editText = dialog.findViewById<EditText>(R.id.search_category)
            val listView = dialog.findViewById<ListView>(R.id.rv_list_category)

            val listAdapter = ArrayAdapter(
                requireContext(),
                androidx.transition.R.layout.support_simple_spinner_dropdown_item, listCategoryName

            )
            listView.adapter = listAdapter
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    listAdapter.filter.filter(s)
                }

                override fun afterTextChanged(s: Editable?) {

                }
            })
            listView.setOnItemClickListener { parent, view, position, id ->
                binding.tvCategory.text = listAdapter.getItem(position)
                binding.tvCategory.text.toString()
                myDatabase.categoryDao().getAllCategory().forEach {
                    if (it.categoryName == binding.tvCategory.text.toString()) {
                        cat = it.categoryId
                        Toast.makeText(requireContext(), "$cat", Toast.LENGTH_SHORT).show()
                    }
                }
                dialog.dismiss()
            }

        }
        binding.btnSave.setOnClickListener {

            val productName = binding.etProduct.text.toString()
            val product = Product(productName = productName, categoryId = cat!!)

            myDatabase.productDao().addProduct(product)


        }


    }

}