package uz.os3ketchup.bozor.presentation.fragments

import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import uz.os3ketchup.bozor.R
import uz.os3ketchup.bozor.data.Product
import uz.os3ketchup.bozor.data.database.MyDatabase
import uz.os3ketchup.bozor.databinding.FragmentProductBinding
import uz.os3ketchup.bozor.presentation.adapters.ProductAdapter
import java.util.*
import kotlin.collections.ArrayList


class ProductFragment : Fragment() {
    private lateinit var binding: FragmentProductBinding
    private lateinit var myDatabase: MyDatabase
    lateinit var productAdapter: ProductAdapter
    lateinit var list: List<Product>

    private lateinit var dialog: Dialog
    private var cat: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        myDatabase = MyDatabase.getInstance(requireContext())
        list = ArrayList()

        myDatabase.productDao().getAllProduct().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                productAdapter = ProductAdapter(requireContext(), it)
                binding.rvProducts.adapter = productAdapter
            }


        val listCategory = myDatabase.categoryDao().getAllCategory()

        val listCategoryName = ArrayList<String>()

        listCategory.forEach {
            listCategoryName.add(it.categoryName)
        }



        binding.tvCategory.setOnClickListener {
            dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.dialog_particular_product)
            dialog.window?.setLayout(ActionBar.LayoutParams.MATCH_PARENT, 800)
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
            listView.setOnItemClickListener { _, _, position, _ ->
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
            if (binding.tvCategory.text.isNotEmpty() && binding.etProduct.text.isNotEmpty()) {
                val productName = binding.etProduct.text.toString().trim()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                val product = Product(productName = productName, categoryId = cat!!)

                myDatabase.productDao().addProduct(product)
                binding.etProduct.text.clear()
            } else {
                Toast.makeText(requireContext(), "please fill empty gaps", Toast.LENGTH_SHORT)
                    .show()
            }


        }


    }


}

