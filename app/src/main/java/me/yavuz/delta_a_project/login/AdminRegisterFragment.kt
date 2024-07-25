package me.yavuz.delta_a_project.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import me.yavuz.delta_a_project.databinding.FragmentAdminRegisterBinding
import me.yavuz.delta_a_project.main.MainActivity
import me.yavuz.delta_a_project.model.User
import me.yavuz.delta_a_project.viewmodel.MainViewModel

class AdminRegisterFragment : Fragment() {

    private lateinit var binding: FragmentAdminRegisterBinding
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onRegisterButtonClicked()
    }

    private fun onRegisterButtonClicked() {
        binding.registerAdminButton.setOnClickListener {
            val name = binding.registerName.text.toString()
            val password = binding.registerPassword.text.toString()
            if (isFieldsNotEmpty(name, password)) {
                viewModel.saveUser("Admin", name, password)
                superAdminCreated()
                observeUser(name, password)
            } else {
                Toast.makeText(
                    binding.root.context,
                    "Please fill all fields!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun isFieldsNotEmpty(name: String, password: String): Boolean {
        return name.isNotEmpty() && password.isNotEmpty()
    }

    private fun observeUser(name: String, password: String) {
        val user = viewModel.getUserByNameAndPassword(name, password)
        user.observe(viewLifecycleOwner) {
            if (it != null) {
                startMainActivity(it)
            }
        }
    }

    private fun startMainActivity(user: User) {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.putExtra("userId", user.id)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun superAdminCreated() {
        val sharedPref = requireActivity().getSharedPreferences("super_admin", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("super_admin", true)
        editor.apply()
    }
}