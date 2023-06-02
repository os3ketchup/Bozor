package uz.os3ketchup.bozor.presentation.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import org.apache.poi.wp.usermodel.HeaderFooterType
import org.apache.poi.xwpf.usermodel.ParagraphAlignment
import org.apache.poi.xwpf.usermodel.XWPFDocument
import uz.os3ketchup.bozor.R
import uz.os3ketchup.bozor.data.OrderProduct
import uz.os3ketchup.bozor.data.ProductInfo
import uz.os3ketchup.bozor.data.SumProduct
import uz.os3ketchup.bozor.data.database.MyDatabase
import uz.os3ketchup.bozor.databinding.FragmentBazarListBinding
import uz.os3ketchup.bozor.presentation.adapters.BazarAdapter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class BazarListFragment : Fragment() {

    lateinit var binding: FragmentBazarListBinding
    private lateinit var bazarAdapter: BazarAdapter
    private lateinit var myDatabase: MyDatabase
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    lateinit var productInfo: ProductInfo


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentBazarListBinding.inflate(inflater, container, false)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("CheckResult", "SetTextI18n", "SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myDatabase = MyDatabase.getInstance(requireActivity())
        /*FirebaseApp.initializeApp(requireContext())
        val database = FirebaseDatabase.getInstance()
        val rootRef = database.reference
        val orderProductRef = rootRef.child("order_products")

        myDatabase.orderProductDao().getAllOrderProduct().forEach {
            orderProductRef.child(it.id.toString()).setValue(it)
        }
*/

        val database = Firebase.database
        val myRef = database.getReference("ORDER_PRODUCTS")
        val list = mutableListOf<OrderProduct>()
        // Read from the database
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (child in dataSnapshot.children) {
                    val orderProduct = child.getValue(OrderProduct::class.java)
                    if (orderProduct != null) {
                        myDatabase.orderProductDao().insertOrUpdate(orderProduct)
                    }
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message

            }
        }
        myRef.addValueEventListener(postListener)




        navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.container_main) as NavHostFragment
        navController = navHostFragment.navController


        var totalPrice = 0.0
        var imageResource = 0


        binding.svProducts.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })
        /*
                val currentDate = LocalDate.now()
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                val formattedDateTime = currentDate.format(formatter)!!
                val orderProduct = myDatabase.orderProductDao().getAllOrderProduct()
        */
        val currentTime = Date()

// Format the time as a string
        val formatter = SimpleDateFormat("yyyy, MMMM d")
        val formattedTime = formatter.format(currentTime)

        binding.ivConfirm.setOnClickListener {
            myDatabase.orderProductDao().getAllOrderProduct().forEach {

                myDatabase.productDao().getAllProducts().forEach { product ->
                    if (it.product == product.productName) {
                        myDatabase.productDao().editProduct(product.copy(productAmount = it.price))
                    }
                }
            }
            val sumProduct = SumProduct(
                date = formattedTime,
                productList = myDatabase.orderProductDao().getAllOrderProduct()
            )
            myDatabase.sumProductDao().addSumProducts(sumProduct)
            myDatabase.productInfoDao().clearTable()
            myDatabase.sumProductDao().getAllSumProducts().forEach {
                it.productList.forEach { requiredProduct ->
                    productInfo = ProductInfo(
                        productName = requiredProduct.product,
                        productPrice = requiredProduct.price,
                        productAmount = requiredProduct.amount,
                        priceDate = it.date,
                        productChanging = 404
                    )

                    myDatabase.productInfoDao().addProductInfo(productInfo)
                }

            }

            myDatabase.orderProductDao().getAllOrderProduct().forEach {
                myDatabase.orderProductDao().editOrderProduct(it.copy(isLongClicked = false))
            }

        }

        binding.fabAddProduct.setOnClickListener {
            findNavController().navigate(R.id.orderFragment)
        }

        binding.ivShare.setOnClickListener {
            val targetDoc = createWordDoc()
            /*addParagraph(targetDoc)*/
            addTable(targetDoc)
            addHeaderAndFooter(targetDoc, totalPrice)
            saveOurDoc(targetDoc)
            val contentUri = saveOurDoc(targetDoc)
            val intent = Intent(Intent.ACTION_SEND)
            intent.type =
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            intent.putExtra(Intent.EXTRA_SUBJECT, "Powered by Eldor")
            intent.putExtra(Intent.EXTRA_STREAM, contentUri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(Intent.createChooser(intent, "Share via"))


        }


        myDatabase.orderProductDao().getAllOrderProducts().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe { orderList ->
                totalPrice = 0.0
                orderList.forEach {
                    if (it.isChecked) {
                        totalPrice += it.sum
                    }
                }
                val formatters = NumberFormat.getCurrencyInstance(Locale.US)
                val formattedAmount = formatters.format(totalPrice)
                binding.tvTotalPrice.text = "Total price: $formattedAmount"

                bazarAdapter = BazarAdapter(requireContext(), orderList, navController)
                binding.rvProducts.adapter = bazarAdapter

                if (imageResource == R.drawable.ic_calendar) {
                    binding.ivAll.setOnClickListener {
                        findNavController().navigate(R.id.action_bazarListFragment_to_dateProducts)
                    }
                } else {
                    binding.ivAll.setOnClickListener {
                        myDatabase.orderProductDao().getAllOrderProduct().forEach {
                            myDatabase.orderProductDao()
                                .editOrderProduct(it.copy(isChecked = !it.isChecked))
                        }
                    }
                }
                binding.ivDelete.setOnClickListener {
                    myDatabase.orderProductDao().getAllOrderProduct().forEach {
                        if (it.isChecked) {
                            myDatabase.orderProductDao().deleteOrderProduct(it)
                            myRef.child(it.id.toString()).removeValue()
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

    private fun filterList(newText: String?) {
        if (newText != null) {
            val filteredList = ArrayList<OrderProduct>()
            for (i in myDatabase.orderProductDao().getAllOrderProduct()) {
                if (i.product.lowercase(Locale.ROOT).contains(newText)) {
                    filteredList.add(i)
                }
            }
            if (filteredList.isEmpty()) {
                Toast.makeText(requireContext(), "no data found", Toast.LENGTH_SHORT).show()
            } else {
                bazarAdapter.setFilteredList(filteredList)
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveOurDoc(targetDoc: XWPFDocument): Uri {
        val ourAppFileDirectory = File(requireActivity().filesDir, "files")
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formatted = currentDateTime.format(formatter)

        if (!ourAppFileDirectory.exists()) {
            ourAppFileDirectory.mkdirs()
        }
        val wordFile = File(ourAppFileDirectory, "bozorlik_$formatted.docx")
        var contentUri: Uri? = null
        try {
            val fileOut = FileOutputStream(wordFile)
            targetDoc.write(fileOut)
            contentUri =
                FileProvider.getUriForFile(
                    requireContext(),
                    "uz.os3ketchup.bozor.provider",
                    wordFile
                )
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return contentUri!!

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addHeaderAndFooter(targetDoc: XWPFDocument, totalPrice: Double) {

        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd ")
        val formatted = currentDateTime.format(formatter)

        //initializing the header
        val docHeader = targetDoc.createHeader(HeaderFooterType.DEFAULT)

        //creating a run for the header. This is for setting the header text and stylings
        val headerRun = docHeader.createParagraph().createRun()
        headerRun.paragraph.alignment = ParagraphAlignment.CENTER

        headerRun.setText(formatted)
        headerRun.fontSize = 24


        headerRun.fontFamily = "Copperplate Gothic"
        headerRun.isBold = true
        headerRun.color = "bc4749"

        //initializing the footer
        val docFooter = targetDoc.createFooter(HeaderFooterType.DEFAULT)

        //creating a run for the footer. This sets the footer text and stylings
        val footerRun = docFooter.createParagraph().createRun()
        footerRun.fontFamily = "Copperplate Gothic"
        footerRun.isBold = true
        val numberFormat = DecimalFormat("#,###")

        val formattedNumber = numberFormat.format(totalPrice)
//formattedNumber is equal to 1,000,000


        footerRun.setText("Sum of products: $formattedNumber so'm")

    }

    /*
        private fun addTable(targetDoc: XWPFDocument) {
            val ourTable = targetDoc.createTable()

            //Creating the first row and adding cell values
            val row1 = ourTable.getRow(0)
            row1.getCell(0).text = "Code"
            row1.addNewTableCell().text = "Item"

            //Creating the second row
            val row2 = ourTable.createRow()
            row2.getCell(0).text = "0345"
            row2.getCell(1).text = "Benz"

            //creating the third row
            val row3 = ourTable.createRow()
            row3.getCell(0).text = "48542"
            row3.getCell(1).text = "Eng-Ed"

        }
    */

    private fun createWordDoc(): XWPFDocument {
        return XWPFDocument()

    }

    private fun addParagraph(targetDoc: XWPFDocument) {
        //creating a paragraph in our document and setting its alignment
        val paragraph1 = targetDoc.createParagraph()
        paragraph1.alignment = ParagraphAlignment.LEFT

        //creating a run for adding text
        val sentenceRun1 = paragraph1.createRun()

        //format the text
        sentenceRun1.isBold = true
        sentenceRun1.fontSize = 15
        sentenceRun1.fontFamily = "Comic Sans MS"
        sentenceRun1.setText("First sentence run starts here. It's such an honour too see you here :-)")
        //add a sentence break
        sentenceRun1.addBreak()

        //add another run
        val sentenceRun2 = paragraph1.createRun()
        sentenceRun2.fontSize = 12
        sentenceRun2.fontFamily = "Comic Sans MS"
        sentenceRun2.setText("Second sentence run starts here. We love Apache POI. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed lacinia dui consectetur euismod ultrices. Aenean et enim pulvinar purus scelerisque dapibus. Duis euismod lorem nec justo viverra ornare. Aliquam est erat, mollis at iaculis eu, ultricies aliquet risus. Proin lacinia ligula sed quam elementum, congue tincidunt lorem iaculis. Nulla facilisi. Praesent faucibus metus eu nisi tincidunt rhoncus vitae et ligula. Pellentesque quam dui, pellentesque vitae placerat eu, tempor ut lectus.")
        sentenceRun2.addBreak()
    }

    private fun addTable(targetDoc: XWPFDocument) {
        val ourTable = targetDoc.createTable()
        ourTable.getRow(0).getCell(0).text = "№"
        ourTable.getRow(0).addNewTableCell().text = "Product Name"
        ourTable.getRow(0).addNewTableCell().text = "Amount"
        ourTable.getRow(0).addNewTableCell().text = "Price"
        ourTable.getRow(0).addNewTableCell().text = "Unit of Amount"
        ourTable.getRow(0).addNewTableCell().text = "Sum"

        ourTable.getRow(0).getCell(0).paragraphs[0].alignment = ParagraphAlignment.CENTER
        ourTable.getRow(0).getCell(1).paragraphs[0].alignment = ParagraphAlignment.CENTER
        ourTable.getRow(0).getCell(2).paragraphs[0].alignment = ParagraphAlignment.CENTER
        ourTable.getRow(0).getCell(3).paragraphs[0].alignment = ParagraphAlignment.CENTER
        ourTable.getRow(0).getCell(4).paragraphs[0].alignment = ParagraphAlignment.CENTER
        ourTable.getRow(0).getCell(5).paragraphs[0].alignment = ParagraphAlignment.CENTER
        val numberFormat = DecimalFormat("#,###")


        for ((inc, i) in myDatabase.orderProductDao().getAllOrderProduct().withIndex()) {

            if (i.isChecked) {
                val formattedNumber = numberFormat.format(i.sum)
                val newRow = ourTable.createRow()

                newRow.getCell(0).text = (inc + 1).toString()
                newRow.getCell(1).text = i.product
                newRow.getCell(2).text = i.amount.toString()
                newRow.getCell(3).text = i.price.toString()
                newRow.getCell(4).text = i.unit
                newRow.getCell(5).text = formattedNumber
            }


        }
    }


}