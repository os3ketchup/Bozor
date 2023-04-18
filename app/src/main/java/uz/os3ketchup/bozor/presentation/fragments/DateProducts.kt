package uz.os3ketchup.bozor.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import uz.os3ketchup.bozor.R
import uz.os3ketchup.bozor.data.database.MyDatabase
import uz.os3ketchup.bozor.databinding.FragmentDateProductsBinding
import uz.os3ketchup.bozor.presentation.adapters.DateAdapter


class DateProducts : Fragment() {
    lateinit var binding: FragmentDateProductsBinding
    lateinit var myDatabase: MyDatabase
    lateinit var dateAdapter: DateAdapter

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDateProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.container_main) as NavHostFragment
        navController = navHostFragment.navController

        myDatabase = MyDatabase.getInstance(requireContext())
        val dateList = myDatabase.sumProductDao().getAllSumProducts()



        dateAdapter = DateAdapter(requireContext(), dateList,navController)
        binding.rvDateAdapter.adapter = dateAdapter
    }

}