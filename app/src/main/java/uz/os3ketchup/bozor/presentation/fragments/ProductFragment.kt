package uz.os3ketchup.bozor.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import uz.os3ketchup.bozor.R
import uz.os3ketchup.bozor.data.Product
import uz.os3ketchup.bozor.data.database.MyDatabase
import uz.os3ketchup.bozor.databinding.FragmentProductBinding
import uz.os3ketchup.bozor.presentation.adapters.ProductAdapter


class ProductFragment : Fragment() {
    private lateinit var binding: FragmentProductBinding
    private lateinit var myDatabase: MyDatabase
    lateinit var productAdapter: ProductAdapter
    lateinit var list: List<Product>

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
        myDatabase = MyDatabase.getInstance(requireContext())
        list = ArrayList()

        myDatabase.productDao().getAllProduct().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                productAdapter = ProductAdapter(requireContext(), it)
                binding.rvProducts.adapter = productAdapter
            }

        binding.ivAdd.setOnClickListener {
            findNavController().navigate(R.id.productAddingFragment)
        }
    }

}