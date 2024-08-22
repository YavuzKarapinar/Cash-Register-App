package me.yavuz.delta_a_project.login

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.postDelayed
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import me.yavuz.delta_a_project.R
import me.yavuz.delta_a_project.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    /**
     * Handler starts after 1.5 sec and checks if super admin is already created.
     *
     * if its created it will navigate to [LoginFragment].
     *
     * if its not created it will navigate to [AdminRegisterFragment].
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.myLooper()!!).postDelayed(1500) {
            if (isSuperAdminCreated()) {
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_adminRegisterFragment)
            }
        }

    }

    /**
     * Checks if super admin is in the shared pref
     *
     * @return if its in the shared pref it will return true
     * if its not in the shared pref it will return false
     */
    private fun isSuperAdminCreated(): Boolean {
        val sharedPref = requireActivity().getSharedPreferences("super_admin", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("super_admin", false)
    }

}