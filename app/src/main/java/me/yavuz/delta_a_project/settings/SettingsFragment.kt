package me.yavuz.delta_a_project.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.yavuz.delta_a_project.adapter.SettingsExpandableListAdapter
import me.yavuz.delta_a_project.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        val group = listOf("STAFF", "MASTER DATA")
        val staffChild = listOf("USER ADD", "USER LIST")
        val masterDataChild = listOf("ADD GROUP", "ADD DEPARTMENT", "ADD TAX", "ADD PRODUCT")
        val childMap = hashMapOf(group[0] to staffChild, group[1] to masterDataChild)

        val adapter = SettingsExpandableListAdapter(binding.root.context, group, childMap)
        binding.expandableListView.setAdapter(adapter)

        return binding.root
    }

}