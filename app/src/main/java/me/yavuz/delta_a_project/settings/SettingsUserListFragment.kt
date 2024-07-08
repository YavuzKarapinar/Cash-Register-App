package me.yavuz.delta_a_project.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import me.yavuz.delta_a_project.adapter.SettingsUserListAdapter
import me.yavuz.delta_a_project.database.DbHelper
import me.yavuz.delta_a_project.databinding.FragmentSettingsUserListBinding

class SettingsUserListFragment : Fragment() {

    private lateinit var binding: FragmentSettingsUserListBinding
    private var itemAdapter = SettingsUserListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dbHelper = DbHelper.getInstance(binding.root.context)
        binding.userListRecyclerView.apply {
            layoutManager = LinearLayoutManager(binding.root.context)
            adapter = itemAdapter
        }

        itemAdapter.setData(dbHelper.getUsers())
        searchFilterListener()
    }

    private fun searchFilterListener() {
        binding.userListSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                itemAdapter.filter.filter(newText)
                return true
            }
        })
    }

}