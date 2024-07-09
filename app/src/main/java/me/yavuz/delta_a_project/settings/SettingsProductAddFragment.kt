package me.yavuz.delta_a_project.settings

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import me.yavuz.delta_a_project.R
import me.yavuz.delta_a_project.database.DbHelper
import me.yavuz.delta_a_project.databinding.FragmentSettingsProductAddBinding
import me.yavuz.delta_a_project.model.Product

class SettingsProductAddFragment : Fragment() {

    private lateinit var binding: FragmentSettingsProductAddBinding
    private lateinit var dbHelper: DbHelper

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

        if (value != null && value != 0) {
            val product = dbHelper.getProductById(value)
            binding.productName.setText(product?.name)
            binding.grossPrice.setText(product?.price.toString())
            binding.productStock.setText(product?.stock.toString())
            binding.productNumber.setText(product?.productNumber.toString())

            binding.productSave.setOnClickListener {
                onUpdateClick()
            }
        } else {
            binding.productSave.setOnClickListener {
                saveOnClick()
            }
        }

        spinnerInitialize()
    }

    private fun spinnerInitialize() {
        val departmentList = dbHelper.getDepartments().map { it.name }
        binding.productDepartmentSpinner.apply {
            adapter = ArrayAdapter(
                binding.root.context,
                R.layout.spinner_item,
                departmentList
            )
        }

        val taxList = dbHelper.getTaxes().map { it.name }
        binding.productTaxSpinner.apply {
            adapter = ArrayAdapter(
                binding.root.context,
                R.layout.spinner_item,
                taxList
            )
        }
    }

    private fun saveOnClick() {
        val name = binding.productName.text.toString()
        val price = binding.grossPrice.text.toString().toDoubleOrNull()
        val stock = binding.productStock.text.toString().toIntOrNull()
        val department = binding.productDepartmentSpinner.selectedItem.toString()
        val tax = binding.productTaxSpinner.selectedItem.toString()
        val productNumber = binding.productNumber.text.toString().toIntOrNull()

        if (TextUtils.isEmpty(name) ||
            TextUtils.isEmpty(price.toString()) ||
            TextUtils.isEmpty(stock.toString()) ||
            TextUtils.isEmpty(productNumber.toString()) ||
            TextUtils.isEmpty(department) ||
            TextUtils.isEmpty(tax)
        ) {
            Toast.makeText(
                binding.root.context,
                "Please fill all fields!",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val product = Product(
                0,
                name,
                price!!,
                stock!!,
                productNumber!!,
                dbHelper.getTaxByName(tax)?.id ?: 0,
                dbHelper.getDepartmentByName(department)?.id ?: 0
            )
            Toast.makeText(
                binding.root.context,
                "Product saved!",
                Toast.LENGTH_SHORT
            ).show()
            dbHelper.saveProduct(product)
        }

    }

    private fun onUpdateClick() {
        /*val name = binding.productName.text.toString()
        val price = binding.grossPrice.text.toString().toDouble()
        val stock = binding.productStock.text.toString().toInt()
        val department = binding.productDepartmentSpinner.selectedItem.toString()
        val tax = binding.productTaxSpinner.selectedItem.toString()
        val productNumber = binding.productNumber.text.toString().toInt()
*/
        Toast.makeText(
            binding.root.context,
            "Product updated!",
            Toast.LENGTH_SHORT
        ).show()
    }
}