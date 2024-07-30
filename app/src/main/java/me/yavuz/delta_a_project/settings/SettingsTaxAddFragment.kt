package me.yavuz.delta_a_project.settings

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import me.yavuz.delta_a_project.adapter.OnActionListener
import me.yavuz.delta_a_project.adapter.SettingsTaxListAdapter
import me.yavuz.delta_a_project.databinding.FragmentSettingsTaxAddBinding
import me.yavuz.delta_a_project.model.Tax
import me.yavuz.delta_a_project.utils.InformationUtils
import me.yavuz.delta_a_project.viewmodel.MainViewModel

class SettingsTaxAddFragment : Fragment() {

    private lateinit var binding: FragmentSettingsTaxAddBinding
    private val viewModel by viewModels<MainViewModel>()
    private val taxListAdapter = SettingsTaxListAdapter()
    private var updateCode = 0
    private var updatePosition: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsTaxAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.taxSave.setOnClickListener {
            if (updateCode == 0) {
                onSaveClick()
            } else if (updateCode == 1) {
                updatePosition?.let { position -> updateClicked(position) }
            }
        }

        binding.taxListRecyclerView.apply {
            adapter = taxListAdapter
            layoutManager = LinearLayoutManager(binding.root.context)
        }

        observeTaxes()

        taxListAdapter.onActionListener = object : OnActionListener {
            override fun onDelete(position: Int) {
                deleteClicked(position)
            }

            override fun onUpdate(position: Int) {
                val tax = taxListAdapter.getData()[position]
                val taxName = tax.name
                val taxValue = tax.value

                binding.taxName.setText(taxName)
                binding.taxValue.setText(taxValue.toString())
                updateCode = 1
                updatePosition = position
                binding.taxSave.text = "Update"
            }

        }
    }

    private fun updateClicked(position: Int) {
        val name = binding.taxName.text.toString()
        val value = binding.taxValue.text.toString()
        val oldTax = taxListAdapter.getData()[position]

        if (name.isEmpty() && value.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "Please fill all fields!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (oldTax.name != name && viewModel.isTaxExists(name)) {
            Toast.makeText(
                binding.root.context,
                "This tax already exists!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val newTax = Tax(oldTax.id, name, value.toDouble())
        viewModel.updateTax(newTax)
        observeTaxes()

        Toast.makeText(
            binding.root.context,
            "Tax updated!",
            Toast.LENGTH_SHORT
        ).show()

        updateCode = 0
        binding.taxSave.text = "Save"
        binding.taxName.setText("")
        binding.taxValue.setText("")
    }

    private fun deleteClicked(position: Int) {
        val taxes = taxListAdapter.getData()
        if (position in taxes.indices) {
            viewModel.deleteTax(taxes[position],
                onSuccess = {
                    observeTaxes()
                    Toast.makeText(
                        context,
                        "Deleted!",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateCode = 0
                    binding.taxSave.text = "Save"
                    binding.taxName.setText("")
                    binding.taxValue.setText("")
                },
                onError = { e ->
                    if (e is SQLiteConstraintException) {
                        InformationUtils.showInfo(
                            requireContext(),
                            "This tax cannot be deleted.\n" +
                                    "Because there is a connection with other data's."
                        )
                    }
                }
            )
        } else {
            Toast.makeText(context, "Tax not found!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeTaxes() {
        viewModel.getTaxes().observe(viewLifecycleOwner) {
            taxListAdapter.setData(it)
        }
    }

    private fun onSaveClick() {
        val name = binding.taxName.text.toString()
        val value = binding.taxValue.text.toString().toDouble()

        if (value !in 0.0..100.0) {
            InformationUtils.showInfo(
                requireContext(),
                "Tax value must be between 0 and 100!"
            )
            return
        }

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(value.toString())) {
            Toast.makeText(
                binding.root.context,
                "Please fill all fields!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (viewModel.isTaxExists(name)) {
            Toast.makeText(
                binding.root.context,
                "This tax already exists!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        Toast.makeText(
                binding.root.context,
                "Tax saved!",
                Toast.LENGTH_SHORT
        ).show()
        viewModel.saveTax(name, value)
        observeTaxes()

        binding.taxName.setText("")
        binding.taxValue.setText("")

    }
}