package uz.os3ketchup.bozor.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import uz.os3ketchup.bozor.R
import uz.os3ketchup.bozor.data.AmountProduct
import uz.os3ketchup.bozor.data.OrderProduct
import uz.os3ketchup.bozor.data.database.MyDatabase
import uz.os3ketchup.bozor.databinding.FragmentBazarListBinding
import uz.os3ketchup.bozor.presentation.adapters.BazarAdapter
import uz.os3ketchup.bozor.presentation.viewmodels.BazarViewModel


class BazarListFragment : Fragment() {

    lateinit var binding: FragmentBazarListBinding
    private lateinit var bazarAdapter: BazarAdapter
    private lateinit var myDatabase: MyDatabase


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentBazarListBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("CheckResult", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var totalPrice = 0.0
        var imageResource = 0



        myDatabase = MyDatabase.getInstance(requireActivity())
        myDatabase.orderProductDao().getAllOrderProducts().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe { orderList ->
                totalPrice = 0.0
                orderList.forEach {
                    if (it.isChecked) {
                        totalPrice += it.sum
                    }
                }
                binding.tvTotalPrice.text = "Total price: $totalPrice"

                bazarAdapter = BazarAdapter(requireContext(), orderList)
                binding.rvProducts.adapter = bazarAdapter



                if (imageResource == R.drawable.ic_calendar) {
                    binding.ivAll.setOnClickListener {
                        Toast.makeText(requireContext(), "calendar", Toast.LENGTH_SHORT).show()
                    }
                } else if (imageResource == R.drawable.ic_all) {
                    binding.ivAll.setOnClickListener {
                        myDatabase.orderProductDao().getAllOrderProduct().forEach {
                            myDatabase.orderProductDao()
                                .editOrderProduct(it.copy(isChecked = !it.isChecked))
                        }
                    }
                }
















                if (orderList[0].isLongClicked) {
                    binding.ivClear.visibility = View.VISIBLE
                    binding.fabAddProduct.visibility = View.INVISIBLE
                    binding.tvTotalPrice.visibility = View.VISIBLE
                    binding.bottomLayout.visibility = View.VISIBLE
                    binding.ivAll.setImageResource(R.drawable.ic_all)
                    imageResource = R.drawable.ic_all

                } else {
                    binding.ivClear.visibility = View.INVISIBLE
                    binding.fabAddProduct.visibility = View.VISIBLE
                    binding.bottomLayout.visibility = View.GONE
                    binding.tvTotalPrice.visibility = View.INVISIBLE
                    binding.ivAll.setImageResource(R.drawable.ic_calendar)
                    imageResource = R.drawable.ic_calendar
                }


            }

        binding.ivClear.setOnClickListener {
            myDatabase.orderProductDao().getAllOrderProduct().forEach {
                myDatabase.orderProductDao().editOrderProduct(it.copy(isLongClicked = false))
            }
        }


    }


}