package me.yavuz.delta_a_project.login

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

        binding.loginButton.setOnClickListener {
            retrieveUserData()
        }
    }

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
}