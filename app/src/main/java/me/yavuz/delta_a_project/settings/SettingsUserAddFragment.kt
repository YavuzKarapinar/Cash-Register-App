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
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import me.yavuz.delta_a_project.R
import me.yavuz.delta_a_project.databinding.FragmentSettingsUserAddBinding
import me.yavuz.delta_a_project.model.User
import me.yavuz.delta_a_project.viewmodel.MainViewModel

class SettingsUserAddFragment : Fragment() {

    private lateinit var binding: FragmentSettingsUserAddBinding
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsUserAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var types: List<String> = mutableListOf()
        val value = arguments?.getInt("userId")

        onPageShow(value)

        val adapter =
            ArrayAdapter(binding.root.context, R.layout.spinner_item, types)
        binding.spinner.adapter = adapter

        viewModel.getUserTypes().observe(viewLifecycleOwner) {
            types = it.map { type -> type.name }
            adapter.clear()
            adapter.addAll(types)
            adapter.notifyDataSetChanged()
        }

        binding.button.setOnClickListener {
            userSave()
        }
    }

    private fun onPageShow(value: Int?) {
        if (value != null && value != 0) {
            lifecycleScope.launch {
                val user = viewModel.getUserById(value)
                user?.let {
                    binding.userSaveName.setText(it.name)
                    binding.saveUserPassword.setText(it.password)
                    binding.saveUserPasswordCorrection.setText(it.password)

                    binding.button.setOnClickListener {
                        lifecycleScope.launch { updateClicked(value) }
                    }
                }
            }
        } else {
            binding.button.setOnClickListener { userSave() }
        }
    }

    private suspend fun updateClicked(value: Int) {
        val position = binding.spinner.selectedItemPosition
        val userType = binding.spinner.getItemAtPosition(position) as String
        val name = binding.userSaveName.text.toString()
        val password = binding.saveUserPassword.text.toString()
        val passwordCorrection = binding.saveUserPasswordCorrection.text.toString()
        val oldUser = viewModel.getUserById(value)

        if (TextUtils.isEmpty(name) ||
            TextUtils.isEmpty(password) ||
            TextUtils.isEmpty(passwordCorrection)
        ) {
            Toast.makeText(
                binding.root.context,
                "Please fill all fields!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (password != passwordCorrection) {
            Toast.makeText(
                binding.root.context,
                "Passwords must be same!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (oldUser?.name != name && viewModel.isUserExists(name)) {
            Toast.makeText(
                binding.root.context,
                "This user already exists!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val newUser = User(value, name, password, userType)
        viewModel.updateUser(newUser)
        Toast.makeText(
            binding.root.context,
            "User updated!",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun userSave() {
        val position = binding.spinner.selectedItemPosition
        val userType = binding.spinner.getItemAtPosition(position) as String
        val name = binding.userSaveName.text.toString()
        val password = binding.saveUserPassword.text.toString()
        val passwordCorrection = binding.saveUserPasswordCorrection.text.toString()

        if(TextUtils.isEmpty(name) ||
            TextUtils.isEmpty(password) ||
            TextUtils.isEmpty(passwordCorrection)) {
            Toast.makeText(
                binding.root.context,
                "Please fill all fields!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (password != passwordCorrection) {
            Toast.makeText(
                binding.root.context,
                "Passwords must be same!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (viewModel.isUserExists(name)) {
            Toast.makeText(
                binding.root.context,
                "This user already exists!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        viewModel.saveUser(userType, name, password)
        Toast.makeText(
            binding.root.context,
            "User saved!",
            Toast.LENGTH_SHORT
        ).show()
    }

}