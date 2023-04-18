package uz.os3ketchup.bozor.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uz.os3ketchup.bozor.R
import uz.os3ketchup.bozor.data.database.MyDatabase
import uz.os3ketchup.bozor.databinding.FragmentAboutListBinding
import uz.os3ketchup.bozor.presentation.adapters.SummarizeAdapter


class AboutListFragment : Fragment() {

    lateinit var binding: FragmentAboutListBinding
    lateinit var myDatabase: MyDatabase
    lateinit var summarizeAdapter: SummarizeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAboutListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myDatabase = MyDatabase.getInstance(requireContext())
        val list = myDatabase.sumProductDao().getAllSumProducts()

        binding.tvDateList.text = myDatabase.sumProductDao().getAllSumProducts()[0].date
        summarizeAdapter = SummarizeAdapter(requireContext(), list)
        binding.rvSummarizeAdapter.adapter = summarizeAdapter
    }

}