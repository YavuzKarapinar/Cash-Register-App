package me.yavuz.delta_a_project.settings

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import me.yavuz.delta_a_project.databinding.FragmentSettingsGroupAddBinding
import me.yavuz.delta_a_project.viewmodel.MainViewModel

class SettingsGroupAddFragment : Fragment() {

    private lateinit var binding: FragmentSettingsGroupAddBinding
    private val viewModel by viewModels<MainViewModel>()

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
            onSaveClick()
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

        Toast.makeText(
            binding.root.context,
            "Group saved!",
            Toast.LENGTH_SHORT
        ).show()

        viewModel.saveGroup(groupName)
    }
}