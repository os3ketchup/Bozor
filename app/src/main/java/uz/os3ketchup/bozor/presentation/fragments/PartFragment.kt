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
import androidx.navigation.fragment.findNavController
import androidx.room.Database
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import uz.os3ketchup.bozor.R
import uz.os3ketchup.bozor.data.Category
import uz.os3ketchup.bozor.data.database.MyDatabase
import uz.os3ketchup.bozor.databinding.FragmentPartBinding
import uz.os3ketchup.bozor.presentation.adapters.CategoryAdapter
import java.util.*
import kotlin.collections.ArrayList


class PartFragment : Fragment() {
    private lateinit var binding: FragmentPartBinding
    private lateinit var categoryAdapter: CategoryAdapter
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

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        myDatabase = MyDatabase.getInstance(requireContext())

        val firebaseDatabase = Firebase.database
        val firebaseRef = firebaseDatabase.getReference("part_products")
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (child in snapshot.children) {
                    val category = child.getValue(Category::class.java)
                    if (category != null) {
                        myDatabase.categoryDao().insertOrUpdate(category)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }

        firebaseRef.addValueEventListener(valueEventListener)
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
            dialog.setCancelable(false)

            btnCancel.setOnClickListener {
                dialog.cancel()
            }

            btnSave.setOnClickListener {
                val list = ArrayList<String>()
                myDatabase.categoryDao().getAllCategory().forEach {
                    list.add(it.categoryName.toString())
                }

                if (editTextName.text.isNotEmpty() && !list.contains(
                        editTextName.text.toString().trim()
                            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                    )
                ) {
                    category = Category(categoryName = editTextName.text.toString().trim()
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() })
                    myDatabase.categoryDao().addCategory(category)
                    val firebaseDatabase = Firebase.database
                    val databaseRef = firebaseDatabase.getReference("part_products")
                    databaseRef.removeValue()
                    myDatabase.categoryDao().getAllCategory().forEach {
                        databaseRef.child(it.categoryId.toString()).setValue(it)
                    }
                    Toast.makeText(requireContext(), "saved", Toast.LENGTH_SHORT).show()
                    dialog.cancel()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "please fill the gaps or this category is already available",
                        Toast.LENGTH_SHORT
                    ).show()
                }


            }

            dialog.show()
        }
    }


}