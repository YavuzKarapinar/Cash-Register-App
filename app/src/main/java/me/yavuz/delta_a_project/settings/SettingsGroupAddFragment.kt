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
import me.yavuz.delta_a_project.adapter.SettingsGroupListAdapter
import me.yavuz.delta_a_project.databinding.FragmentSettingsGroupAddBinding
import me.yavuz.delta_a_project.model.Group
import me.yavuz.delta_a_project.utils.InformationUtils
import me.yavuz.delta_a_project.viewmodel.MainViewModel

class SettingsGroupAddFragment : Fragment() {

    private lateinit var binding: FragmentSettingsGroupAddBinding
    private val viewModel by viewModels<MainViewModel>()
    private val groupListAdapter = SettingsGroupListAdapter()
    private var updateCode = 0
    private var updatePosition: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsGroupAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.groupSave.setOnClickListener {
            if (updateCode == 0) {
                onSaveClick()
            } else if (updateCode == 1) {
                updatePosition?.let { position -> updateClicked(position) }
            }
        }

        groupListAdapterSetup()
    }

    private fun groupListAdapterSetup() {
        binding.groupListRecyclerView.apply {
            adapter = groupListAdapter
            layoutManager = LinearLayoutManager(binding.root.context)
        }

        observeGroups()

        groupListAdapter.onActionListener = object : OnActionListener {
            override fun onDelete(position: Int) {
                deleteClicked(position)
            }

            override fun onUpdate(position: Int) {
                val group = groupListAdapter.getData()[position]
                val groupName = group.name

                binding.groupName.setText(groupName)
                updateCode = 1
                updatePosition = position
                binding.groupSave.text = "Update"
            }
        }
    }

    private fun deleteClicked(position: Int) {
        val groups = groupListAdapter.getData()
        if (position in groups.indices) {
            viewModel.deleteGroup(groups[position],
                onSuccess = {
                    observeGroups()
                    Toast.makeText(
                        context,
                        "Deleted!",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateCode = 0
                    binding.groupSave.text = "Save"
                    binding.groupName.setText("")
                },
                onError = { e ->
                    if (e is SQLiteConstraintException) {
                        InformationUtils.showInfo(
                            requireContext(),
                            "This group cannot be deleted.\n" +
                                    "Because there is a connection with other data's."
                        )
                    }
                }
            )
        } else {
            Toast.makeText(context, "Group not found!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateClicked(position: Int) {
        val name = binding.groupName.text.toString()
        val oldGroup = groupListAdapter.getData()[position]

        if (name.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "Please fill all fields!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (oldGroup.name != name && viewModel.isGroupExists(name)) {
            Toast.makeText(
                binding.root.context,
                "This group already exists!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val newGroup = Group(oldGroup.id, name)
        viewModel.updateGroup(newGroup)
        observeGroups()

        Toast.makeText(
            binding.root.context,
            "Group updated!",
            Toast.LENGTH_SHORT
        ).show()

        updateCode = 0
        binding.groupSave.text = "Save"
        binding.groupName.setText("")
    }

    private fun observeGroups() {
        viewModel.getGroups().observe(viewLifecycleOwner) {
            groupListAdapter.setData(it)
        }
    }

    private fun onSaveClick() {
        val groupName = binding.groupName.text.toString()

        if (TextUtils.isEmpty(groupName)) {
            Toast.makeText(
                binding.root.context,
                "Please fill all fields!",
                Toast.LENGTH_SHORT
            ).show()
        }

        if (viewModel.isGroupExists(groupName)) {
            Toast.makeText(
                binding.root.context,
                "This group already exists!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        viewModel.saveGroup(groupName)
        observeGroups()

        Toast.makeText(
            binding.root.context,
            "Group saved!",
            Toast.LENGTH_SHORT
        ).show()

        binding.groupName.setText("")
    }
}