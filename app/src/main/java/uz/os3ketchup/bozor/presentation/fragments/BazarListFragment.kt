package uz.os3ketchup.bozor.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import uz.os3ketchup.bozor.data.AmountProduct
import uz.os3ketchup.bozor.data.database.MyDatabase
import uz.os3ketchup.bozor.databinding.FragmentBazarListBinding
import uz.os3ketchup.bozor.presentation.adapters.BazarAdapter
import uz.os3ketchup.bozor.presentation.viewmodels.BazarViewModel


class BazarListFragment : Fragment() {

    lateinit var binding: FragmentBazarListBinding
    private lateinit var bazarAdapter: BazarAdapter
    private lateinit var myDatabase: MyDatabase
    private var total = 0
    private val model: BazarViewModel by viewModels()
    private lateinit var amountList: ArrayList<AmountProduct>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentBazarListBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        myDatabase = MyDatabase.getInstance(requireActivity())
        amountList = ArrayList()
        myDatabase.sumProductDao().getAllSumProducts().forEach { sumProduct ->
            amountList.addAll(sumProduct.productList)
        }

        binding.ivConfirm.setOnClickListener {

        }

        val summaryObserver = Observer<Int> { totalPrice ->
            binding.tvTotalPrice.text = totalPrice.toString()
        }
        myDatabase.amountProductDao().getAllAmountProduct().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                it.forEach { amount ->
                    total += amount.priceProduct.toInt()
                }
            }

        model.totalProduct.value = total

        model.totalProduct.observe(requireActivity(), summaryObserver)
        myDatabase.sumProductDao().getAllSumProduct().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe { listSumProduct ->

                bazarAdapter = BazarAdapter(requireContext(), amountList, listSumProduct)

                binding.rvProducts.adapter = bazarAdapter

                if (listSumProduct[0].isSelected) {
                    binding.ivClear.visibility = View.VISIBLE
                    binding.fabAddProduct.visibility = View.INVISIBLE
                    binding.tvTotalPrice.visibility = View.VISIBLE
                    binding.bottomLayout.visibility = View.VISIBLE
                } else {
                    binding.ivClear.visibility = View.INVISIBLE
                    binding.fabAddProduct.visibility = View.VISIBLE
                    binding.bottomLayout.visibility = View.GONE
                    binding.tvTotalPrice.visibility = View.INVISIBLE
                }

            }

        binding.ivClear.setOnClickListener {
            myDatabase.sumProductDao().getAllSumProducts().forEach {
                myDatabase.sumProductDao().editSumProduct(it.copy(isSelected = false))
            }
        }


    }


}