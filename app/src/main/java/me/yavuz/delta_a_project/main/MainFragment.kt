package me.yavuz.delta_a_project.main

import android.animation.LayoutTransition
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
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
import me.yavuz.delta_a_project.utils.CalculateUtils
import me.yavuz.delta_a_project.utils.InformationUtils
import me.yavuz.delta_a_project.viewmodel.MainViewModel
import me.yavuz.delta_a_project.viewmodel.SharedViewModel
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Showing main menu for ui look for layout and see also
 *
 * @see FragmentMainBinding
 */
class MainFragment : Fragment() {

    private val builder: StringBuilder = StringBuilder()
    private val pluBuilder: StringBuilder = StringBuilder()
    private lateinit var decimalFormat: DecimalFormat
    private lateinit var binding: FragmentMainBinding
    private val mainItemAdapter by lazy { MainItemAdapter { addToCart(it) } }
    private val viewModel by viewModels<MainViewModel>()
    private val cartItems: MutableList<Pair<Product, Int>> = mutableListOf()
    private lateinit var cartAdapter: CartAdapter
    private val sharedViewModel by activityViewModels<SharedViewModel>()

    private companion object {
        const val ZERO_NUMBER = "0.00"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Setting up ui items when ui elements created, observing products for adding to ui and
     * initializing decimal format type
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        decimalFormat = DecimalFormat("#,###.##")
        setupUI()
        observeProduct()
    }

    /**
     * Setting up ui
     */
    private fun setupUI() {
        setupSearchFilter()
        setupButtonClickListeners()
        expandNumberKeyboard()
        setupCartRecycler()
        setupItemRecycler()
        setupPaymentButtons()
        setupPluButton()
    }

    /**
     * Setting plu button. This plu property is for adding items to cart for more than one.
     *
     * If user clicks number for quantity and after that clicks X button and clicks numbers for
     * product number it will take that much items from database and add to cart
     *
     * For example: 11X1 -> that means product number 1 and 11 pieces of that product
     */
    private fun setupPluButton() {
        binding.pluButton.setOnClickListener {
            lifecycleScope.launch {
                if (!pluBuilder.contains("X")) {
                    InformationUtils.showInfo(
                        requireContext(),
                        "Correct Usage: Quantity -> X -> Product Number -> Plu"
                    )
                    pluBuilderClear()
                    return@launch
                }

                val split = pluBuilder.toString().split("X")

                if (split[0].isEmpty() || split[1].isEmpty()) {
                    InformationUtils.showInfo(
                        requireContext(),
                        "Correct Usage: Quantity -> X -> Product Number -> Plu"
                    )
                    pluBuilderClear()
                    return@launch
                }

                val quantity = split[0].toInt()
                val productNumber = split[1].toInt()

                val product =
                    withContext(Dispatchers.IO) {
                        viewModel.getProductByProductNumber(
                            productNumber
                        )
                    }

                if (product == null || (quantity <= 0 || quantity >= product.stock)) {
                    InformationUtils.showInfo(
                        requireContext(),
                        "Product is null or Quantity is not suitable"
                    )
                    pluBuilderClear()
                    return@launch
                }

                if (mainItemAdapter.currentList.find { it.id == product.id }!!.stock <= 0) {
                    InformationUtils.showInfo(
                        requireContext(),
                        "Stock is not enough!"
                    )
                    pluBuilderClear()
                    return@launch
                }

                mainItemAdapter.currentList.find { it.id == product.id }!!.stock -= quantity
                mainItemAdapter.setData(mainItemAdapter.currentList)
                cartAdapter.updateItem(product, quantity)
                mainItemAdapter.notifyDataSetChanged()
                updateTotalPrice()
                binding.showNumbers.text = ZERO_NUMBER
                pluBuilder.clear()
                builder.clear()
            }
        }
    }

    /**
     * Setting up payment buttons.
     *
     * When user clicks cash, card, other or return buttons it will trigger this method
     */
    private fun setupPaymentButtons() {
        binding.cashPaymentButton.setOnClickListener { processPayment(1) }
        binding.cardPaymentButton.setOnClickListener { processPayment(2) }
        binding.otherPaymentButton.setOnClickListener { processPayment(3) }
        binding.returnButton.setOnClickListener { processReturn() }
    }

    /**
     * When user clicks return button it will take items from cart and make their quantity as a
     * negative number and show it on the ui.
     */
    private fun processReturn() {
        cartItems.forEachIndexed { index, pair ->
            if(pair.second > 0) {
                cartItems[index] = Pair(pair.first, -pair.second)
            }
        }
        cartAdapter.notifyDataSetChanged()
        updateTotalPrice()
    }

    /**
     * When user clicks payment buttons it will process this method. With this method we will take
     * user id from shared view model and send it to process cart items method and also show the receipt
     *
     * @param type takes type 1. cash, 2. card, 3. other
     *
     * @see processCartItems
     * @see SharedViewModel
     */
    private fun processPayment(type: Int) {
        if (cartItems.isNotEmpty()) {
            binding.cashPaymentButton.isClickable = false
            binding.cardPaymentButton.isClickable = false
            binding.otherPaymentButton.isClickable = false
            sharedViewModel.data.observe(viewLifecycleOwner) { userId ->
                lifecycleScope.launch {
                    showReceipt(type)
                    processCartItems(userId, type)
                }
            }
        }
    }

    /**
     * Shows receipt on the ui with sale information
     *
     * @param type the type that is used for payment 1. cash, 2. card, 3. other
     */
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
        builder.setOnDismissListener {
            binding.cashPaymentButton.isClickable = true
            binding.cardPaymentButton.isClickable = true
            binding.otherPaymentButton.isClickable = true
            clearViews()
        }
        builder.show()
    }

    /**
     * Setting receipt total prices. This will be showed on the receipt alert dialog.
     */
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

    /**
     * Calculates total taxes
     *
     * @return total tax
     */
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

    /**
     * Builds tax details for showing on the receipt alert dialog
     */
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

    /**
     * Setting receipt date and time with [SimpleDateFormat]
     */
    private fun setReceiptDateAndTime(alertBinding: ReceiptDialogBinding) {
        val date = Date()
        alertBinding.date.text = SimpleDateFormat.getDateInstance().format(date)
        alertBinding.clock.text = SimpleDateFormat.getTimeInstance().format(date)
    }

    /**
     * Inflating receipt alert dialog
     *
     * @return [ReceiptDialogBinding]
     */
    private fun inflateReceiptDialog(): ReceiptDialogBinding {
        val customLayout = layoutInflater.inflate(R.layout.receipt_dialog, binding.root, false)
        val alertBinding = ReceiptDialogBinding.bind(customLayout)
        alertBinding.cartItemsRecycler.apply {
            adapter = ReceiptItemAdapter(cartItems)
            layoutManager = LinearLayoutManager(alertBinding.root.context)
        }
        return alertBinding
    }

    /**
     * Processing cart items by its selling type
     *
     * if quantity is negative it will assign return
     *
     * if quantity is positive it will assign sale
     *
     * @param userId user id
     * @param sellingType selling type. For more information see also
     *
     * @see [me.yavuz.delta_a_project.model.SellingProcessType]
     */
    private suspend fun processCartItems(userId: Int, sellingType: Int) {
        val itemsToProcess = cartItems.toList()
        itemsToProcess.forEach { item ->
            val sellingFormat = if (item.second > 0) "SALE" else "RETURN"
            saveSellingProcess(item, sellingFormat, userId, sellingType)
        }
    }

    /**
     * Saves selling process to database for more information see also
     *
     * @param item pair item to be saved
     * @param sellingFormat selling format to be saved "return" or "sale"
     * @param userId user id
     * @param sellingType selling type to ve saved "cash", "card" or "other"s
     *
     * @see [SellingProcess]
     * @see [MainViewModel.saveSellingProcess]
     */
    private suspend fun saveSellingProcess(
        item: Pair<Product, Int>,
        sellingFormat: String,
        userId: Int,
        sellingType: Int
    ) {
        val tax = viewModel.getTaxById(item.first.taxId)
        val netPrice = CalculateUtils.calculateNetPrice(item.first.price, tax!!.value)
        val zId = viewModel.getLastZNumber().takeIf { it > 0 } ?: viewModel.insertReportZ()
        val xId = viewModel.getLastXNumber().takeIf { it > 0 } ?: viewModel.insertReportX(zId)
        val sellingProcess = SellingProcess(
            id = 0,
            quantity = item.second,
            priceSell = CalculateUtils.formatDouble(netPrice).toDouble(),
            sellingFormat = sellingFormat,
            zId = zId,
            xId = xId,
            userId = userId,
            sellingProcessTypeId = sellingType,
            productId = item.first.id
        )

        updateProductStock(item)
        viewModel.saveSellingProcess(sellingProcess)
    }

    /**
     * Updates product stocks with its pair.
     *
     * If pair's second one is negative it will add to the product
     *
     * If pair's second one is positive it will remove from the product
     *
     * @param item item pair to be be updated.
     */
    private suspend fun updateProductStock(item: Pair<Product, Int>) {
        val product = viewModel.getProductById(item.first.id)
        product?.let {
            it.stock -= item.second
            viewModel.updateProduct(it)
        }
    }

    /**
     * Clears views after selling products
     */
    private fun clearViews() {
        cartItems.clear()
        cartAdapter.notifyDataSetChanged()
        binding.apply {
            val total = "Total: $ZERO_NUMBER"
            mainTotalPrice.text = total
            showNumbers.text = ZERO_NUMBER
        }
        builder.clear()
        pluBuilder.clear()
        observeProduct()
    }

    private fun pluBuilderClear() {
        binding.apply {
            showNumbers.text = ZERO_NUMBER
        }
        builder.clear()
        pluBuilder.clear()
    }

    /**
     * Setting up cart recycler. Adding Vertical Divider to the recycler view
     */
    private fun setupCartRecycler() {
        cartAdapter = CartAdapter(cartItems)
        binding.cartRecyclerView.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(binding.root.context)
            addItemDecoration(DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            ))
        }
    }

    /**
     * Setting up item recycler. Adding Vertical Divider to the recycler view
     */
    private fun setupItemRecycler() {
        binding.itemRecyclerView.apply {
            layoutManager = LinearLayoutManager(binding.root.context)
            adapter = mainItemAdapter
            addItemDecoration(DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            ))
        }
    }

    /**
     * Observing products.
     */
    private fun observeProduct() {
        viewModel.getProducts().observe(viewLifecycleOwner) {
            mainItemAdapter.setData(it)
        }
    }

    /**
     * Setting up search field. On Query Text Change it will send filtered text to main item adapter.
     * If there is an item with that name it will show to ui.
     */
    private fun setupSearchFilter() {
        binding.mainItemSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                mainItemAdapter.filter.filter(newText)
                return true
            }
        })
    }

    /**
     * Setting up number pad button click listeners.
     *
     * If number clicked it will append to builder.
     *
     * If left aux (plu x button) clicked it will append pluBuilder X
     *
     * If right aux (delete button) clicked it will delete one character from the builders.
     */
    private fun setupButtonClickListeners() {
        binding.cardLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        binding.numberKeyboard.setListener(object : NumberKeyboardListener {
            override fun onNumberClicked(number: Int) {
                appendToBuilder(number.toString())
            }

            override fun onLeftAuxButtonClicked() {
                if (builder.isNotEmpty() && !pluBuilder.contains("X")) {
                    pluBuilder.append("X")
                    builder.clear()
                    binding.showNumbers.text = ZERO_NUMBER
                }
            }

            override fun onRightAuxButtonClicked() {
                if (builder.isNotEmpty()) {
                    if (builder.isNotEmpty()) {
                        builder.deleteCharAt(builder.length - 1)
                    }
                    if (pluBuilder.isNotEmpty()) {
                        pluBuilder.deleteCharAt(pluBuilder.length - 1)
                    }
                    updateTextView()
                }
            }
        })
    }

    /**
     * Append texts to builders.
     *
     * @param text text for appending
     */
    private fun appendToBuilder(text: String) {
        builder.append(text)
        pluBuilder.append(text)
        updateTextView()
    }

    /**
     * Updates text view when buttons clicked
     */
    private fun updateTextView() {
        val value = builder.toString().toDoubleOrNull()?.div(100.0) ?: 0.00
        binding.showNumbers.text = decimalFormat.format(value)
    }

    /**
     * Adds to card when clicked to product.
     *
     * @param product product to add
     */
    private fun addToCart(product: Product) {
        cartAdapter.updateItem(product)
        updateTotalPrice()
    }

    /**
     * Updating total price text view when items added or removed from cart.
     */
    private fun updateTotalPrice() {
        val totalPrice = CalculateUtils.calculateTotalPrice(cartItems)
        val total = "Total: ${CalculateUtils.formatDouble(totalPrice)}"
        binding.mainTotalPrice.text = total
    }

    /**
     * Expands or Collapse number keyboard when clicked to expand text view
     *
     *
     */
    private fun expandNumberKeyboard() {
        binding.expandText.setOnClickListener {
            val numpadVisibility =
                if (binding.numberKeyboard.visibility == View.GONE) View.VISIBLE else View.GONE
            TransitionManager.beginDelayedTransition(binding.cardLayout, AutoTransition())
            TransitionManager.beginDelayedTransition(binding.cardLayout, AutoTransition())
            binding.numberKeyboard.visibility = numpadVisibility
            binding.expandText.text =
                if (numpadVisibility == View.GONE) "Expand Numpad" else "Collapse Numpad"
        }
    }
}