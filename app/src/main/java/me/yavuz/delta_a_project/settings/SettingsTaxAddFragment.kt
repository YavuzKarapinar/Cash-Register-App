package me.yavuz.delta_a_project.settings

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import me.yavuz.delta_a_project.databinding.FragmentSettingsTaxAddBinding
import me.yavuz.delta_a_project.viewmodel.MainViewModel

class SettingsTaxAddFragment : Fragment() {

    private lateinit var binding: FragmentSettingsTaxAddBinding
    private val viewModel by viewModels<MainViewModel>()

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
            onSaveClick()
        }
    }

    private fun onSaveClick() {
        val name = binding.taxName.text.toString()
        val value = binding.taxValue.text.toString().toDouble()

        if (value !in 0.0..100.0) {
            Toast.makeText(
                binding.root.context,
                "Tax value must be between 0 and 100!",
                Toast.LENGTH_SHORT
            ).show()

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
    }
}