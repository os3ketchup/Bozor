package uz.os3ketchup.bozor.presentation.fragments

import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.room.Database
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import uz.os3ketchup.bozor.R
import uz.os3ketchup.bozor.data.Category
import uz.os3ketchup.bozor.data.database.MyDatabase
import uz.os3ketchup.bozor.databinding.FragmentPartBinding
import uz.os3ketchup.bozor.presentation.adapters.CategoryAdapter


class PartFragment : Fragment() {
    private lateinit var binding: FragmentPartBinding
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var list: ArrayList<Category>
    private lateinit var myDatabase: MyDatabase
    lateinit var category: Category


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPartBinding.inflate(inflater, container, false)
        return binding.root
    }


    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myDatabase = MyDatabase.getInstance(requireContext())
        myDatabase.categoryDao().getAllCategories().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                categoryAdapter = CategoryAdapter(requireContext(), it)
                binding.rvPartAdapter.adapter = categoryAdapter
            }


        binding.ivAdd.setOnClickListener {
            val dialog = Dialog(requireContext())

            dialog.setContentView(R.layout.dialog_part)
            val editTextName: EditText = dialog.findViewById(R.id.et_part_name)
            val btnCancel: Button = dialog.findViewById(R.id.btn_cancel)
            val btnSave: Button = dialog.findViewById(R.id.btn_save)
            dialog.window?.setLayout(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT
            )

            btnCancel.setOnClickListener {
                dialog.cancel()
            }
            btnSave.setOnClickListener {
                list = ArrayList()

                val category = Category(categoryName = editTextName.text.toString())

                if (editTextName.text.isNotEmpty()) {
                    myDatabase.categoryDao().addCategory(category)
                    Toast.makeText(requireContext(), "saved", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "please fill the gaps", Toast.LENGTH_SHORT)
                        .show()
                }
                list.add(category)

                dialog.cancel()
            }

            dialog.show()
        }
    }


}