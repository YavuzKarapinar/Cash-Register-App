package me.yavuz.delta_a_project.settings

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import me.yavuz.delta_a_project.R
import me.yavuz.delta_a_project.adapter.OnActionListener
import me.yavuz.delta_a_project.adapter.SettingsUserListAdapter
import me.yavuz.delta_a_project.databinding.FragmentSettingsUserListBinding
import me.yavuz.delta_a_project.viewmodel.MainViewModel
import me.yavuz.delta_a_project.viewmodel.SharedViewModel

class SettingsUserListFragment : Fragment() {

    private lateinit var binding: FragmentSettingsUserListBinding
    private var itemAdapter = SettingsUserListAdapter()
    private val viewModel by viewModels<MainViewModel>()
    private val sharedViewModel by activityViewModels<SharedViewModel>()

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

        sharedViewModel.data.observe(viewLifecycleOwner) {
            itemAdapter.onActionListener = object : OnActionListener {
                override fun onDelete(position: Int) {
                    onDeleteClicked(position, it)
                }

                override fun onUpdate(position: Int) {
                    onUpdateClicked(position)
                }
            }
        }

        searchFilterListener()
    }

    private fun onDeleteClicked(position: Int, id: Int) {
        val users = itemAdapter.getData()
        if (position in users.indices && users[position].id != id) {
            viewModel.deleteUser(users[position],
                onSuccess = {
                    observeUsers()
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
                            "This user cannot be deleted!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
        } else {
            Toast.makeText(
                context,
                "This user logged in, cannot be deleted!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun onUpdateClicked(position: Int) {
        val users = itemAdapter.getData()
        if (position in users.indices) {
            val fragment = SettingsUserAddFragment()
            val bundle = Bundle().apply {
                putInt("userId", users[position].id)
            }
            fragment.arguments = bundle
            parentFragmentManager.beginTransaction()
                .replace(R.id.settingsFragmentContainer, fragment, "userId")
                .commit()
        } else {
            Toast.makeText(context, "Product not found!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeUsers() {
        viewModel.getUsers().observe(viewLifecycleOwner) { users ->
            itemAdapter.setData(users)
        }
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