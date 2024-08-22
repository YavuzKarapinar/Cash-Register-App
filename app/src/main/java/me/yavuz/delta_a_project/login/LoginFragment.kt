package me.yavuz.delta_a_project.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import me.yavuz.delta_a_project.databinding.FragmentLoginBinding
import me.yavuz.delta_a_project.main.MainActivity
import me.yavuz.delta_a_project.viewmodel.MainViewModel

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retrieveRememberMe()
        binding.loginButton.setOnClickListener {
            retrieveUserData()
        }
    }

    /**
     * Checks if name and password is in the database.
     *
     * if its in the database it will start [MainActivity].
     *
     * if its not in the database it will say "Username or password is wrong".
     */
    private fun retrieveUserData() {
        if (!TextUtils.isEmpty(binding.loginName.text) &&
            !TextUtils.isEmpty(binding.loginPassword.text)
        ) {

            val user = viewModel.getUserByNameAndPassword(
                binding.loginName.text.toString(),
                binding.loginPassword.text.toString()
            )

            user.observe(viewLifecycleOwner) {
                if (it != null) {
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    intent.putExtra("userId", it.id)
                    rememberMe(it.name)
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Username or password wrong!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    /**
     * Retrieves remember me shared pref for checking if there is any saved name.
     *
     * If its saved it will set name field with that name.
     */
    private fun retrieveRememberMe() {
        val sharedPref = requireActivity().getSharedPreferences("remember_me", Context.MODE_PRIVATE)
        sharedPref.getString("remember_me", null)?.let {
            binding.loginName.setText(it)
        } ?: binding.loginName.setText("")
    }

    /**
     * Putting name to remember me shared pref.
     *
     * @param name name for saving to shared pref.
     */
    private fun rememberMe(name: String) {
        val sharedPref = requireActivity().getSharedPreferences("remember_me", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("remember_me", name)
        editor.apply()
    }
}