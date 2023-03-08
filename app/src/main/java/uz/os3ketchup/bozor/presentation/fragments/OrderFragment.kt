package uz.os3ketchup.bozor.presentation.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.LinearLayout.LayoutParams
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import uz.os3ketchup.bozor.R
import uz.os3ketchup.bozor.data.AmountProduct
import uz.os3ketchup.bozor.data.OrderProduct
import uz.os3ketchup.bozor.data.SumProduct
import uz.os3ketchup.bozor.data.database.MyDatabase
import uz.os3ketchup.bozor.databinding.FragmentOrderBinding
import uz.os3ketchup.bozor.presentation.adapters.AmountProductAdapter


class OrderFragment : Fragment(), AdapterView.OnItemSelectedListener {
    lateinit var binding: FragmentOrderBinding
    private lateinit var myDatabase: MyDatabase
    private lateinit var dialog: Dialog
    private var selectedCategoryId: Int = 0
    private lateinit var amountProductAdapter: AmountProductAdapter
    var productUnits: String = "SASA"
    lateinit var category: String
    lateinit var product: String
    lateinit var sumProductList: ArrayList<AmountProduct>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOrderBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myDatabase = MyDatabase.getInstance(requireActivity())

        myDatabase.amountProductDao().getAllAmountProduct().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe { productList ->
                amountProductAdapter = AmountProductAdapter(requireContext(), productList)
                binding.rvAmounts.adapter = amountProductAdapter
            }

        binding.btnSave.setOnClickListener {
            val amountProduct = AmountProduct(
                productCategory = binding.tvPart.text.toString(),
                productName = binding.tvNameOfProduct.text.toString(),
                amountProduct = binding.etAmount.text.toString(),
                unitProduct = productUnits
            )
            myDatabase.amountProductDao().addAmountProduct(amountProduct)


        }

        val listCategory = myDatabase.categoryDao().getAllCategory()
        val listCategoryName = ArrayList<String>()
        val productNameList = ArrayList<String>()

        val spinner = binding.spinnerUnit
        spinner.onItemSelectedListener = this
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.planets_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }


        listCategory.forEach {
            listCategoryName.add(it.categoryName)
        }

        binding.addProduct.setOnClickListener {
            findNavController().navigate(R.id.productFragment)
        }
        binding.addPart.setOnClickListener {
            findNavController().navigate(R.id.partFragment)
        }

        binding.ivOrder.setOnClickListener {
            Toast.makeText(requireContext(), "clicked", Toast.LENGTH_SHORT).show()

//            val productList = myDatabase.amountProductDao().getAllAmountProducts()
//
//            val sumProduct = SumProduct(sumProduct = 90.0, productList = productList)
//            myDatabase.sumProductDao().addSumProducts(sumProduct)

            myDatabase.amountProductDao().getAllAmountProducts().forEach {
                val orderProduct = OrderProduct(
                    category = it.productCategory,
                    product = it.productName,
                    unit = it.unitProduct,
                    sum = (it.priceProduct * it.amountProduct.toInt()),
                    price = 0.0,
                    amount = it.amountProduct.toDouble(),
                    date = ""
                )

                myDatabase.orderProductDao().addOrderProduct(orderProduct)
                myDatabase.amountProductDao().clearTable()
            }


        }

        binding.tvPart.setOnClickListener {
            dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.dialog_particular_product)
            dialog.window?.setLayout(LayoutParams.MATCH_PARENT, 800)
            dialog.show()

            val editText = dialog.findViewById<EditText>(R.id.search_category)!!
            val listView = dialog.findViewById<ListView>(R.id.rv_list_category)!!

            val listAdapter = ArrayAdapter(
                requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                listCategoryName
            )
            listView.adapter = listAdapter
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    listAdapter.filter.filter(s)
                }

                override fun afterTextChanged(s: Editable?) {

                }
            })

            listView.setOnItemClickListener { _, _, position, _ ->
                binding.tvPart.text = listAdapter.getItem(position)
                binding.tvPart.text.toString()
                category = listAdapter.getItem(position).toString()
                myDatabase.categoryDao().getAllCategory().forEach {
                    if (it.categoryName == binding.tvPart.text.toString()) {
                        selectedCategoryId = it.categoryId!!

                        myDatabase.productDao().getAllProducts().forEach { product ->
                            if (product.categoryId == selectedCategoryId) {
                                productNameList.add(product.productName)
                            }
                        }
                    }
                }
                dialog.dismiss()

            }

        }

        binding.tvNameOfProduct.setOnClickListener {


            dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.dialog_particular_product)
            dialog.window?.setLayout(LayoutParams.MATCH_PARENT, 800)
            dialog.show()


            val editText = dialog.findViewById<EditText>(R.id.search_category)!!
            val listView = dialog.findViewById<ListView>(R.id.rv_list_category)!!

            val listAdapter = ArrayAdapter(
                requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                productNameList
            )

            listView.adapter = listAdapter

            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    listAdapter.filter.filter(s)
                }

                override fun afterTextChanged(s: Editable?) {

                }
            })

            listView.setOnItemClickListener { _, _, position, _ ->
                binding.tvNameOfProduct.text = listAdapter.getItem(position)
                product = listAdapter.getItem(position).toString()
                dialog.dismiss()
            }

        }


    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        productUnits = parent?.getItemAtPosition(position).toString()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
}