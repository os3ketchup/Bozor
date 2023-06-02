package uz.os3ketchup.bozor.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uz.os3ketchup.bozor.R
import uz.os3ketchup.bozor.data.ProductInfo
import uz.os3ketchup.bozor.data.database.MyDatabase
import uz.os3ketchup.bozor.databinding.FragmentInfoProductBinding
import uz.os3ketchup.bozor.presentation.adapters.InfoProductAdapter
import uz.os3ketchup.bozor.presentation.adapters.ProductAdapter


class InfoProductFragment : Fragment() {

    lateinit var binding: FragmentInfoProductBinding
    lateinit var myDatabase: MyDatabase
    lateinit var infoProductAdapter: InfoProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentInfoProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mySpecialList = ArrayList<ProductInfo>()
        myDatabase = MyDatabase.getInstance(requireContext())
        val productName = arguments?.getString("product")!!
        binding.tvProductName.text = productName
        val list = myDatabase.productInfoDao().getAllProductInfo()
        list.forEach {
            if (productName == it.productName) {
                mySpecialList.add(it)
            }
        }
        infoProductAdapter = InfoProductAdapter(requireContext(), mySpecialList)
        binding.rvProductInfo.adapter = infoProductAdapter
    }

}