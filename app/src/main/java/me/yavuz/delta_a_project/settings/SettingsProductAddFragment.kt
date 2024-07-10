package me.yavuz.delta_a_project.settings

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import me.yavuz.delta_a_project.R
import me.yavuz.delta_a_project.database.DbHelper
import me.yavuz.delta_a_project.databinding.FragmentSettingsProductAddBinding
import me.yavuz.delta_a_project.model.Product
import me.yavuz.delta_a_project.viewmodel.MainViewModel

class SettingsProductAddFragment : Fragment() {

    private lateinit var binding: FragmentSettingsProductAddBinding
    private lateinit var dbHelper: DbHelper
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsProductAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHelper = DbHelper.getInstance(binding.root.context)
        val value = arguments?.getInt("productId")
        dbHelper = DbHelper.getInstance(binding.root.context)

        onPageShow(value)
        departmentSpinnerInitialize()
        taxSpinnerInitialize()
    }

    private fun onPageShow(value: Int?) {
        lifecycleScope.launch {
            if (value != null && value != 0) {
                val product = dbHelper.getProductById(value)
                binding.productName.setText(product?.name)
                binding.grossPrice.setText(product?.price.toString())
                binding.productStock.setText(product?.stock.toString())
                binding.productNumber.setText(product?.productNumber.toString())

                binding.productSave.setOnClickListener {
                    if (product != null) {
                        lifecycleScope.launch { onUpdateClick(product.id) }
                    }
                }
            } else {
                binding.productSave.setOnClickListener {
                    lifecycleScope.launch { saveOnClick() }
                }
            }
        }

    }

    private fun departmentSpinnerInitialize() {
        var departmentList: List<String> = mutableListOf()
        val departmentAdapter = ArrayAdapter(
            binding.root.context,
            R.layout.spinner_item,
            departmentList
        )

        binding.productDepartmentSpinner.apply {
            adapter = departmentAdapter
        }

        viewModel.getDepartments().observe(viewLifecycleOwner) {
            departmentList = it.map { department -> department.name }
            departmentAdapter.clear()
            departmentAdapter.addAll(departmentList)
            departmentAdapter.notifyDataSetChanged()
        }
    }

    private fun taxSpinnerInitialize() {
        var taxList: List<String> = mutableListOf()
        val taxAdapter = ArrayAdapter(
            binding.root.context,
            R.layout.spinner_item,
            taxList
        )

        binding.productTaxSpinner.apply {
            adapter = taxAdapter
        }

        viewModel.getTaxes().observe(viewLifecycleOwner) {
            taxList = it.map { tax -> tax.name }
            taxAdapter.clear()
            taxAdapter.addAll(taxList)
            taxAdapter.notifyDataSetChanged()
        }
    }

    private suspend fun saveOnClick() {
        val name = binding.productName.text.toString()
        val price = binding.grossPrice.text.toString()
        val stock = binding.productStock.text.toString()
        val department = binding.productDepartmentSpinner.selectedItem.toString()
        val tax = binding.productTaxSpinner.selectedItem.toString()
        val productNumber = binding.productNumber.text.toString()
        if (isFieldsEmpty(name, price, stock, productNumber, department, tax)) {
            Toast.makeText(
                binding.root.context,
                "Please fill all fields!",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val product = Product(
                0,
                name,
                price.toDouble(),
                stock.toInt(),
                productNumber.toInt(),
                viewModel.getTaxByName(tax)?.id ?: 0,
                viewModel.getDepartmentByName(name)?.id ?: 0
            )
            Toast.makeText(
                binding.root.context,
                "Product saved!",
                Toast.LENGTH_SHORT
            ).show()
            dbHelper.saveProduct(product)
        }

    }

    private suspend fun onUpdateClick(productId: Int) {
        val name = binding.productName.text.toString()
        val price = binding.grossPrice.text.toString()
        val stock = binding.productStock.text.toString()
        val department = binding.productDepartmentSpinner.selectedItem.toString()
        val tax = binding.productTaxSpinner.selectedItem.toString()
        val productNumber = binding.productNumber.text.toString()

        if (isFieldsEmpty(name, price, stock, productNumber, department, tax)) {
            Toast.makeText(
                binding.root.context,
                "Please fill all fields!",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val newProduct = Product(
                productId,
                name,
                price.toDouble(),
                stock.toInt(),
                productNumber.toInt(),
                viewModel.getTaxByName(tax)?.id ?: 0,
                viewModel.getDepartmentByName(department)?.id ?: 0
            )
            Toast.makeText(
                binding.root.context,
                "Product updated!",
                Toast.LENGTH_SHORT
            ).show()
            dbHelper.updateProduct(newProduct)
        }
    }

    private fun isFieldsEmpty(
        name: String,
        price: String,
        stock: String,
        productNumber: String,
        department: String,
        tax: String
    ): Boolean {
        return TextUtils.isEmpty(name) ||
                TextUtils.isEmpty(price) ||
                TextUtils.isEmpty(stock) ||
                TextUtils.isEmpty(productNumber) ||
                TextUtils.isEmpty(department) ||
                TextUtils.isEmpty(tax)
    }
}