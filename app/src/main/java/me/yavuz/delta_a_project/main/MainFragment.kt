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
import kotlinx.coroutines.launch
import me.yavuz.delta_a_project.R
import me.yavuz.delta_a_project.adapter.CartAdapter
import me.yavuz.delta_a_project.adapter.MainItemAdapter
import me.yavuz.delta_a_project.adapter.ReceiptItemAdapter
import me.yavuz.delta_a_project.databinding.FragmentMainBinding
import me.yavuz.delta_a_project.databinding.ReceiptDialogBinding
import me.yavuz.delta_a_project.model.Product
import me.yavuz.delta_a_project.model.SellingProcess
import me.yavuz.delta_a_project.utils.CalculateUtils
import me.yavuz.delta_a_project.viewmodel.MainViewModel
import me.yavuz.delta_a_project.viewmodel.SharedViewModel
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date

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
        setupUI()
        observeProduct()
    }

    private fun setupUI() {
        setupSearchFilter()
        setupButtonClickListeners()
        setupCartRecycler()
        setupItemRecycler()
        setupPaymentButtons()
    }

    private fun setupPaymentButtons() {
        binding.cashPaymentButton.setOnClickListener { processPayment(1) }
        binding.cardPaymentButton.setOnClickListener { processPayment(2) }
        binding.otherPaymentButton.setOnClickListener { processPayment(3) }
        binding.returnButton.setOnClickListener { processReturn() }
    }

    private fun processReturn() {
        cartItems.forEachIndexed { index, pair ->
            if(pair.second > 0) {
                cartItems[index] = Pair(pair.first, -pair.second)
            }
        }
        cartAdapter.notifyDataSetChanged()
        updateTotalPrice()
    }

    private fun processPayment(type: Int) {
        if (cartItems.isNotEmpty()) {
            sharedViewModel.data.observe(viewLifecycleOwner) { userId ->
                lifecycleScope.launch {
                    showReceipt(type)
                    processCartItems(userId, type)
                }
            }
        }
    }

    private fun showReceipt(type: Int) {
        val builder = AlertDialog.Builder(binding.root.context)
        val alertBinding = inflateReceiptDialog()

        setReceiptDateAndTime(alertBinding)
        setReceiptTotalPrices(alertBinding, type)

        builder.setView(alertBinding.root)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            clearViews()
        }
        builder.setOnDismissListener { clearViews() }
        builder.show()
    }

    private fun setReceiptTotalPrices(alertBinding: ReceiptDialogBinding, type: Int) {
        lifecycleScope.launch {
            val totalTax = calculateTotalTax()
            val totalPrice = CalculateUtils.calculateTotalPrice(cartItems)
            val netPrice = totalPrice - totalTax
            val paymentInfoBuilder = StringBuilder()
            val paymentPriceBuilder = StringBuilder()
            val taxDetails = buildTaxDetails()

            paymentInfoBuilder.append("\nPayed \n")
                .append("Total Tax \n")
                .append("Net Price \n")
                .append(taxDetails.first)

            paymentPriceBuilder.append("\n${CalculateUtils.formatDouble(totalPrice)}\n")
                .append("${CalculateUtils.formatDouble(totalTax)}\n")
                .append("${CalculateUtils.formatDouble(netPrice)}\n")
                .append(taxDetails.second)

            alertBinding.apply {
                paymentInformation.text = paymentInfoBuilder.toString()
                paymentInformationPrice.text = paymentPriceBuilder.toString()
                totalSellingPrice.text = CalculateUtils.formatDouble(totalPrice)
                totalPriceProcessRow.text = CalculateUtils.formatDouble(totalPrice)
                sellingType.text = viewModel.getSellingTypeById(type)?.name ?: "Other"
            }
        }
    }

    private fun calculateTotalTax(): Double {
        var totalTax = 0.0
        cartAdapter.cartList.forEach {
            val tax = viewModel.getTaxById(it.first.taxId)
            val taxPrice =
                it.first.price - CalculateUtils.calculateNetPrice(it.first.price, tax!!.value)
            totalTax += taxPrice
        }
        return totalTax
    }

    private suspend fun buildTaxDetails(): Pair<String, String> {
        val taxInfoBuilder = StringBuilder()
        val taxPriceBuilder = StringBuilder()
        val taxPriceMap = mutableMapOf<String, Double>()

        cartAdapter.cartList.forEach {
            val tax = viewModel.getTaxById(it.first.taxId)
            val taxPrice =
                it.first.price - CalculateUtils.calculateNetPrice(it.first.price, tax!!.value)

            taxPriceMap[tax.name] = taxPriceMap.getOrDefault(tax.name, 0.0) + taxPrice
        }

        taxPriceMap.forEach { (name, price) ->
            taxInfoBuilder.append("$name %${viewModel.getTaxByName(name)!!.value}\n")
            taxPriceBuilder.append("${CalculateUtils.formatDouble(price)}\n")
        }

        return Pair(taxInfoBuilder.toString(), taxPriceBuilder.toString())
    }

    private fun setReceiptDateAndTime(alertBinding: ReceiptDialogBinding) {
        val date = Date()
        alertBinding.date.text = SimpleDateFormat.getDateInstance().format(date)
        alertBinding.clock.text = SimpleDateFormat.getTimeInstance().format(date)
    }

    private fun inflateReceiptDialog(): ReceiptDialogBinding {
        val customLayout = layoutInflater.inflate(R.layout.receipt_dialog, binding.root, false)
        val alertBinding = ReceiptDialogBinding.bind(customLayout)
        alertBinding.cartItemsRecycler.apply {
            adapter = ReceiptItemAdapter(cartItems)
            layoutManager = LinearLayoutManager(alertBinding.root.context)
        }
        return alertBinding
    }

    private suspend fun processCartItems(userId: Int, sellingType: Int) {
        cartItems.forEach { item ->
            val sellingFormat = if (item.second > 0) "SALE" else "RETURN"
            saveSellingProcess(item, sellingFormat, userId, sellingType)
        }
    }

    private suspend fun saveSellingProcess(
        item: Pair<Product, Int>,
        sellingFormat: String,
        userId: Int,
        sellingType: Int
    ) {
        val tax = viewModel.getTaxById(item.first.taxId)
        val netPrice = CalculateUtils.calculateNetPrice(item.first.price, tax!!.value)
        val sellingProcess = SellingProcess(
            id = 0,
            quantity = item.second,
            priceSell = CalculateUtils.formatDouble(netPrice).toDouble(),
            sellingFormat = sellingFormat,
            zId = viewModel.getLastZNumber(),
            userId = userId,
            sellingProcessTypeId = sellingType,
            productId = item.first.id
        )

        updateProductStock(item)
        viewModel.saveSellingProcess(sellingProcess)
    }

    private suspend fun updateProductStock(item: Pair<Product, Int>) {
        val product = viewModel.getProductById(item.first.id)
        product?.let {
            it.stock -= item.second
            viewModel.updateProduct(it)
        }
    }

    private fun clearViews() {
        cartItems.clear()
        cartAdapter.notifyDataSetChanged()
        binding.apply {
            mainTotalPrice.text = "Total: 0.00"
            showNumbers.text = "0.00"
        }
        builder.clear()
        observeProduct()
    }

    private fun setupCartRecycler() {
        cartAdapter = CartAdapter(cartItems)
        binding.cartRecyclerView.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(binding.root.context)
        }
    }

    private fun setupItemRecycler() {
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

    private fun setupSearchFilter() {
        binding.mainItemSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                mainItemAdapter.filter.filter(newText)
                return true
            }
        })
    }

    private fun setupButtonClickListeners() {
        binding.numberKeyboard.setListener(object : NumberKeyboardListener {
            override fun onNumberClicked(number: Int) {
                appendToBuilder(number.toString())
            }

            override fun onLeftAuxButtonClicked() {
                Toast.makeText(binding.root.context, "Multiply", Toast.LENGTH_SHORT).show()
                // TODO: Implement multiply with PLU
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
        val value = builder.toString().toDoubleOrNull()?.div(100.0) ?: 0.00
        binding.showNumbers.text = decimalFormat.format(value)
    }

    private fun addToCart(product: Product) {
        cartAdapter.updateItem(product)
        updateTotalPrice()
    }

    private fun updateTotalPrice() {
        val totalPrice = CalculateUtils.calculateTotalPrice(cartItems)
        binding.mainTotalPrice.text = "Total: ${CalculateUtils.formatDouble(totalPrice)}"
    }
}