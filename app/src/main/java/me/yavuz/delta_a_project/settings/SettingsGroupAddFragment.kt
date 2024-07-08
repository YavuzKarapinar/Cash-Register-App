package me.yavuz.delta_a_project.settings

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import me.yavuz.delta_a_project.database.DbHelper
import me.yavuz.delta_a_project.databinding.FragmentSettingsGroupAddBinding

class SettingsGroupAddFragment : Fragment() {

    private lateinit var binding: FragmentSettingsGroupAddBinding

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
        val dbHelper = DbHelper.getInstance(binding.root.context)

        if (TextUtils.isEmpty(groupName)) {
            Toast.makeText(
                binding.root.context,
                "Please fill all fields!",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                binding.root.context,
                "Group saved!",
                Toast.LENGTH_SHORT
            ).show()

            dbHelper.saveGroup(groupName)
        }
    }
}