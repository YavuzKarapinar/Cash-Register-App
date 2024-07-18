package me.yavuz.delta_a_project.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.davidmiguel.numberkeyboard.NumberKeyboardListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.yavuz.delta_a_project.R
import me.yavuz.delta_a_project.adapter.CartAdapter
import me.yavuz.delta_a_project.adapter.MainItemAdapter
import me.yavuz.delta_a_project.adapter.ReceiptItemAdapter
import me.yavuz.delta_a_project.databinding.FragmentMainBinding
import me.yavuz.delta_a_project.databinding.ReceiptDialogBinding
import me.yavuz.delta_a_project.model.Product
import me.yavuz.delta_a_project.model.SellingProcess
import me.yavuz.delta_a_project.viewmodel.MainViewModel
import me.yavuz.delta_a_project.viewmodel.SharedViewModel
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainFragment : Fragment() {

    private val builder: StringBuilder = StringBuilder()
    private lateinit var decimalFormat: DecimalFormat
    private lateinit var binding: FragmentMainBinding
    private val mainItemAdapter by lazy { MainItemAdapter { addToCart(it) } }
    private val viewModel by viewModels<MainViewModel>()
    private val cartItems: MutableList<Pair<Product, Int>> = mutableListOf()
    private lateinit var cartAdapter: CartAdapter
    private val sharedViewModel by activityViewModels<SharedViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        decimalFormat = DecimalFormat("#,###.##")
        searchFilterListener()
        buttonClickListeners()
        cartRecyclerInitialize()
        itemRecyclerInitialize()
        observeProduct()
        onPaymentClick()
    }

    private fun onPaymentClick() {
        binding.paymentButton.setOnClickListener {
            if (cartItems.isNotEmpty()) {
                sharedViewModel.data.observe(viewLifecycleOwner) { userId ->
                    lifecycleScope.launch {
                        receiptShow()
                        cartForEachItem(userId)
                        clearViews()
                    }
                }
            }
        }
    }

    private fun receiptShow() {
        val builder = AlertDialog.Builder(binding.root.context)
        builder.setTitle("Payment Successful")

        val alertBinding = receiptDialogBinding()

        setReceiptDateAndClock(alertBinding)
        setReceiptTotalPrices(alertBinding)

        builder.setView(alertBinding.root)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun setReceiptTotalPrices(alertBinding: ReceiptDialogBinding) {
        val totalPrice = cartItems.sumOf { it.first.price * it.second }
        val totalPriceFormatted = String.format(Locale.getDefault(), "%.1f", totalPrice)
        alertBinding.totalSellingPrice.text = totalPriceFormatted
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val sellingType = viewModel.getSellingTypeById(1)
                alertBinding.sellingType.text = sellingType?.name ?: "Other"
            }
        }
    }

    private fun setReceiptDateAndClock(alertBinding: ReceiptDialogBinding) {
        val date = Date(System.currentTimeMillis())
        alertBinding.date.text = SimpleDateFormat.getDateInstance().format(date)
        alertBinding.clock.text = SimpleDateFormat.getTimeInstance().format(date)
    }

    private fun receiptDialogBinding(): ReceiptDialogBinding {
        val customLayout =
            layoutInflater.inflate(R.layout.receipt_dialog, binding.root, false)
        val alertBinding = ReceiptDialogBinding.bind(customLayout)
        alertBinding.cartItemsRecycler.apply {
            adapter = ReceiptItemAdapter(cartItems)
            layoutManager = LinearLayoutManager(alertBinding.root.context)
        }
        return alertBinding
    }

    private suspend fun cartForEachItem(userId: Int) {
        cartItems.forEach { item ->
            val tax =
                withContext(Dispatchers.IO) { viewModel.getTaxById(item.first.taxId) }
            val priceSell = (item.first.price) / ((tax!!.value / 100) + 1)
            val sellingProcess = SellingProcess(
                id = 0,
                quantity = item.second,
                priceSell = String.format(Locale.getDefault(), "%.1f", priceSell).toDouble(),
                userId = userId,
                sellingProcessTypeId = 1,
                productId = item.first.id
            )
            val product = viewModel.getProductById(item.first.id)
            product!!.stock -= item.second

            viewModel.updateProduct(product!!)
            viewModel.saveSellingProcess(sellingProcess)
        }
    }

    private fun clearViews() {
        cartItems.clear()
        cartAdapter.notifyDataSetChanged()
        binding.mainTotalPrice.text = "Total: 0.00"
        binding.showNumbers.text = "0.00"
        observeProduct()
        builder.clear()
    }

    private fun cartRecyclerInitialize() {
        cartAdapter = CartAdapter(cartItems)
        binding.cartRecyclerView.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(binding.root.context)
        }
    }

    private fun itemRecyclerInitialize() {
        binding.itemRecyclerView.apply {
            layoutManager = LinearLayoutManager(binding.root.context)
            adapter = mainItemAdapter
        }
    }

    private fun observeProduct() {
        viewModel.getProducts().observe(viewLifecycleOwner) {
            mainItemAdapter.setData(it)
        }
    }

    private fun searchFilterListener() {
        binding.mainItemSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                mainItemAdapter.filter.filter(newText)
                return true
            }
        })
    }

    private fun buttonClickListeners() {
        binding.numberKeyboard.setListener(object : NumberKeyboardListener {
            override fun onNumberClicked(number: Int) {
                appendToBuilder(number.toString())
            }

            override fun onLeftAuxButtonClicked() {
                Toast.makeText(
                    binding.root.context,
                    "Multiply",
                    Toast.LENGTH_SHORT
                ).show() // todo add multiply with plu
            }

            override fun onRightAuxButtonClicked() {
                if (builder.isNotEmpty()) {
                    builder.deleteCharAt(builder.length - 1)
                    updateTextView()
                }
            }
        })
    }

    private fun appendToBuilder(text: String) {
        builder.append(text)
        updateTextView()
    }

    private fun updateTextView() {
        val value = builder.takeIf { it.isNotEmpty() }.toString().toDoubleOrNull()?.div(100.0)

        if (value != null) {
            binding.showNumbers.text = decimalFormat.format(value)
        } else {
            binding.showNumbers.text = "0.00"
        }
    }

    private fun addToCart(product: Product) {
        cartAdapter.updateItem(product)
        val totalPrice = cartItems.sumOf { it.first.price * it.second }
        val totalPriceFormatted = String.format(Locale.getDefault(), "%.1f", totalPrice)
        binding.mainTotalPrice.text = "Total: $totalPriceFormatted"
    }
}
