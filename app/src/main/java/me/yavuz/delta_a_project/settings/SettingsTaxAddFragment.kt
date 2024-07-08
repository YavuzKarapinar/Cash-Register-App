package me.yavuz.delta_a_project.settings

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import me.yavuz.delta_a_project.database.DbHelper
import me.yavuz.delta_a_project.databinding.FragmentSettingsTaxAddBinding

class SettingsTaxAddFragment : Fragment() {

    private lateinit var binding: FragmentSettingsTaxAddBinding
    private lateinit var dbHelper: DbHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsTaxAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHelper = DbHelper.getInstance(binding.root.context)

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
        } else {
            Toast.makeText(
                binding.root.context,
                "Tax saved!",
                Toast.LENGTH_SHORT
            ).show()
            dbHelper.saveTax(name, value)
        }
    }
}