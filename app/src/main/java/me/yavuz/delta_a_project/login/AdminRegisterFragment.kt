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
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    /**
     * When user clicked register button this method will invoke. On this method it will control
     * name and password edit texts.
     *
     * If these fields is empty it will show a toast message saying
     * "Please fill all the fields".
     *
     * If it is not empty then it will check that user is already registered to database if its not
     * it will insert that user to database. Also it will save this user as a remember me and
     * super admin will not showcased after that moment.
     */
    private fun onRegisterButtonClicked() {
        binding.registerAdminButton.setOnClickListener {
            lifecycleScope.launch {
                val name = binding.registerName.text.toString()
                val password = binding.registerPassword.text.toString()
                if (isFieldsNotEmpty(name, password)) {
                    binding.registerAdminButton.isClickable = false
                    val userExists = withContext(Dispatchers.IO) {
                        viewModel.isUserExists(name)
                    }

                    if (userExists) {
                        Toast.makeText(
                            binding.root.context,
                            "This user already exists",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.registerAdminButton.isClickable = true
                        return@launch
                    }

                    withContext(Dispatchers.IO) {
                        viewModel.saveUser("Admin", name, password)
                    }
                    observeUser(name, password)
                    superAdminCreated()
                    rememberMe(name)
                } else {
                    Toast.makeText(
                        binding.root.context,
                        "Please fill all fields!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    /**
     * Checks if edit text fields is empty or not.
     *
     * @param name name edit text string
     * @param password password edit text string
     *
     * @return if it's not empty true if it's empty false
     */
    private fun isFieldsNotEmpty(name: String, password: String): Boolean {
        return name.isNotEmpty() && password.isNotEmpty()
    }

    /**
     * Observes user based on its name and password. If its null it will call [startMainActivity]
     * method.
     *
     * @param name name edit text string
     * @param password password edit text string
     */
    private fun observeUser(name: String, password: String) {
        val user = viewModel.getUserByNameAndPassword(name, password)
        user.observe(viewLifecycleOwner) {
            if (it != null) {
                startMainActivity(it)
            }
        }
    }

    /**
     * Starts main activity and add user id to its intent.
     *
     * @param user User for adding its id.
     */
    private fun startMainActivity(user: User) {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.putExtra("userId", user.id)
        startActivity(intent)
        requireActivity().finish()
    }

    /**
     * Admin Register Fragment will showed to user if its false. This method makes it true to not showed again.
     */
    private fun superAdminCreated() {
        val sharedPref = requireActivity().getSharedPreferences("super_admin", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("super_admin", true)
        editor.apply()
    }

    /**
     * Remember Me functionality added with this method. If its called it will add name to its shared pref.
     *
     * @param name the string to be saved in the shared pref.
     */
    private fun rememberMe(name: String) {
        val sharedPref = requireActivity().getSharedPreferences("remember_me", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("remember_me", name)
        editor.apply()
    }
}