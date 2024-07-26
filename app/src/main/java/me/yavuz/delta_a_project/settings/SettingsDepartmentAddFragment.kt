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
import me.yavuz.delta_a_project.R
import me.yavuz.delta_a_project.databinding.FragmentSettingsDepartmentAddBinding
import me.yavuz.delta_a_project.viewmodel.MainViewModel

class SettingsDepartmentAddFragment : Fragment() {

    private lateinit var binding: FragmentSettingsDepartmentAddBinding
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsDepartmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arrayAdapterObserve()
        binding.departmentSave.setOnClickListener {
            val name = binding.departmentName.text.toString()
            val group = binding.departmentSpinner.selectedItem as String
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
            return
        }

        if (viewModel.isDepartmentExists(name)) {
            Toast.makeText(
                binding.root.context,
                "This department already exists!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        Toast.makeText(
            binding.root.context,
            "Department saved!",
            Toast.LENGTH_SHORT
        ).show()

        viewModel.saveDepartment(group, name)
    }

    private fun arrayAdapterObserve() {
        var groups: List<String> = mutableListOf()

        val arrayAdapter = ArrayAdapter(
            binding.root.context,
            R.layout.spinner_item,
            groups
        )
        binding.departmentSpinner.adapter = arrayAdapter

        viewModel.getGroups().observe(viewLifecycleOwner) {
            groups = it.map { group -> group.name }
            arrayAdapter.clear()
            arrayAdapter.addAll(groups)
            arrayAdapter.notifyDataSetChanged()
        }
    }
}