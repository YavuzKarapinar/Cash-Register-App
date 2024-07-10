package me.yavuz.delta_a_project.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import me.yavuz.delta_a_project.adapter.SettingsUserListAdapter
import me.yavuz.delta_a_project.databinding.FragmentSettingsUserListBinding
import me.yavuz.delta_a_project.viewmodel.MainViewModel

class SettingsUserListFragment : Fragment() {

    private lateinit var binding: FragmentSettingsUserListBinding
    private var itemAdapter = SettingsUserListAdapter()
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.userListRecyclerView.apply {
            layoutManager = LinearLayoutManager(binding.root.context)
            adapter = itemAdapter
        }

        viewModel.getUsers().observe(viewLifecycleOwner) {
            itemAdapter.setData(it)
        }

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