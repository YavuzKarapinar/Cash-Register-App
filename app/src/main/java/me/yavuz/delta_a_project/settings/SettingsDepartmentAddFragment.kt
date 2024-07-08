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
import me.yavuz.delta_a_project.databinding.FragmentSettingsDepartmentAddBinding

class SettingsDepartmentAddFragment : Fragment() {

    private lateinit var binding: FragmentSettingsDepartmentAddBinding
    private lateinit var dbHelper: DbHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsDepartmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHelper = DbHelper.getInstance(binding.root.context)
        val groupList = dbHelper.getGroups().map { it.name }

        binding.departmentSpinner.apply {
            adapter = ArrayAdapter(
                binding.root.context,
                R.layout.spinner_item,
                groupList
            )
        }

        binding.departmentSave.setOnClickListener {
            val name = binding.departmentName.text.toString()
            val position = binding.departmentSpinner.selectedItemPosition
            val group = binding.departmentSpinner.getItemAtPosition(position) as String
            departmentSaveOnClick(group, name)
        }
    }

    private fun departmentSaveOnClick(group: String, name: String) {
        if (TextUtils.isEmpty(group)) {
            Toast.makeText(
                binding.root.context,
                "Please fill all fields!",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                binding.root.context,
                "Department saved!",
                Toast.LENGTH_SHORT
            ).show()

            dbHelper.saveDepartment(group, name)
        }
    }
}