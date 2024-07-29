package me.yavuz.delta_a_project.settings

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import me.yavuz.delta_a_project.R
import me.yavuz.delta_a_project.adapter.OnActionListener
import me.yavuz.delta_a_project.adapter.SettingsDepartmentListAdapter
import me.yavuz.delta_a_project.databinding.FragmentSettingsDepartmentAddBinding
import me.yavuz.delta_a_project.model.Department
import me.yavuz.delta_a_project.viewmodel.MainViewModel

class SettingsDepartmentAddFragment : Fragment() {

    private lateinit var binding: FragmentSettingsDepartmentAddBinding
    private val viewModel by viewModels<MainViewModel>()
    private val departmentListAdapter = SettingsDepartmentListAdapter()
    private var updateCode = 0
    private var updatePosition: Int? = null

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
            if (updateCode == 0) {
                val name = binding.departmentName.text.toString()
                val group = binding.departmentSpinner.selectedItem as String
                departmentSaveOnClick(group, name)
            } else if (updateCode == 1) {
                updatePosition?.let { position -> updateClicked(position) }
            }
        }

        binding.departmentListRecyclerView.apply {
            adapter = departmentListAdapter
            layoutManager = LinearLayoutManager(binding.root.context)
        }

        observeDepartments()

        departmentListAdapter.onActionListener = object : OnActionListener {
            override fun onDelete(position: Int) {
                deleteClicked(position)
            }

            override fun onUpdate(position: Int) {
                val department = departmentListAdapter.getData()[position]
                val departmentName = department.name

                binding.departmentName.setText(departmentName)
                updateCode = 1
                updatePosition = position
                binding.departmentSave.text = "Update"
            }
        }
    }

    private fun updateClicked(position: Int) {
        val name = binding.departmentName.text.toString()
        val oldDepartment = departmentListAdapter.getData()[position]

        if (name.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "Please fill all fields!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (oldDepartment.name != name && viewModel.isDepartmentExists(name)) {
            Toast.makeText(
                binding.root.context,
                "This department already exists!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val newDepartment = Department(oldDepartment.id, oldDepartment.groupId, name)
        viewModel.updateDepartment(newDepartment)
        observeDepartments()

        Toast.makeText(
            binding.root.context,
            "Department updated!",
            Toast.LENGTH_SHORT
        ).show()

        updateCode = 0
        binding.departmentSave.text = "Save"
        binding.departmentName.setText("")
    }

    private fun deleteClicked(position: Int) {
        val departments = departmentListAdapter.getData()
        if (position in departments.indices) {
            viewModel.deleteDepartment(departments[position],
                onSuccess = {
                    observeDepartments()
                    Toast.makeText(
                        context,
                        "Deleted!",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onError = { e ->
                    if (e is SQLiteConstraintException) {
                        Toast.makeText(
                            context,
                            "This department cannot be deleted!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
        } else {
            Toast.makeText(context, "Department not found!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeDepartments() {
        viewModel.getDepartments().observe(viewLifecycleOwner) {
            departmentListAdapter.setData(it)
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
        observeDepartments()
        binding.departmentName.setText("")
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