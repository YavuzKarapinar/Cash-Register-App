package me.yavuz.delta_a_project.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import me.yavuz.delta_a_project.R
import me.yavuz.delta_a_project.adapter.SettingsExpandableListAdapter
import me.yavuz.delta_a_project.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val group = listOf("STAFF", "MASTER DATA", "REPORT")
        val staffChild = listOf("USER ADD", "USER LIST")
        val masterDataChild = listOf(
            "ADD GROUP",
            "ADD DEPARTMENT",
            "ADD TAX",
            "ADD PRODUCT",
            "LIST PRODUCT"
        )
        val reportChild = listOf("REPORT Z", "REPORT X")
        val childMap = hashMapOf(
            group[0] to staffChild,
            group[1] to masterDataChild,
            group[2] to reportChild
        )

        val adapter = SettingsExpandableListAdapter(binding.root.context, group, childMap)
        binding.expandableListView.setAdapter(adapter)
        onChildClick(childMap, group)
    }

    private fun onChildClick(childMap: HashMap<String, List<String>>, group: List<String>) {
        binding.expandableListView.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->
            val selectedFragment = when (childMap[group[groupPosition]]?.get(childPosition)) {
                "USER ADD" -> SettingsUserAddFragment()
                "USER LIST" -> SettingsUserListFragment()
                "ADD GROUP" -> SettingsGroupAddFragment()
                "ADD DEPARTMENT" -> SettingsDepartmentAddFragment()
                "ADD TAX" -> SettingsTaxAddFragment()
                "ADD PRODUCT" -> SettingsProductAddFragment()
                "LIST PRODUCT" -> SettingsProductListFragment()
                "REPORT Z" -> SettingsReportZFragment()
                "REPORT X" -> SettingsReportXFragment()
                else -> Fragment()
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.settingsFragmentContainer, selectedFragment)
                .commit()

            false
        }
    }
}