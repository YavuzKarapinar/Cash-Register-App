package me.yavuz.delta_a_project.main

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import kotlinx.coroutines.launch
import me.yavuz.delta_a_project.R
import me.yavuz.delta_a_project.databinding.ActivityMainBinding
import me.yavuz.delta_a_project.viewmodel.MainViewModel
import me.yavuz.delta_a_project.viewmodel.SharedViewModel

/**
 * For displaying [MainFragment] and [SettingsFragment]
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private val sharedViewModel by viewModels<SharedViewModel>()
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupBottomNav()

        val dataFromLogin = getDataFromLogin()
        setVisibilityForStaff(dataFromLogin)
    }

    /**
     * Setting up bottom navigation bar, nav host fragment and nav controller.
     */
    private fun setupBottomNav() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.mainFrame) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
        fragmentNavigation()
    }

    /**
     * Navigating from main activity to main menu fragment, settings fragment and logout from app.
     */
    private fun fragmentNavigation() {
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.mainFragment -> {
                    navController.navigate(R.id.mainFragment)
                    true
                }

                R.id.settingsFragment -> {
                    navController.navigate(R.id.settingsFragment)
                    true
                }

                R.id.loginActivity -> {
                    navController.navigate(R.id.loginActivity)
                    rememberMe()
                    finish()
                    true
                }

                else -> {
                    false
                }
            }
        }
    }

    /**
     * Getting Data's from login activity.
     *
     * If taken user id is not empty it will set shared view model data for
     * more info look for see also
     * @see SharedViewModel
     *
     * @return saved user id
     */
    private fun getDataFromLogin(): Int {
        val dataFromLogin = intent.getIntExtra("userId", 0)
        dataFromLogin.takeIf { it != 0 }.let { sharedViewModel.setData(it!!) }
        return dataFromLogin
    }

    /**
     * Setting visibility for staff if taken user is staff it will not show setting because of the
     * authority
     */
    private fun setVisibilityForStaff(dataFromLogin: Int) {
        lifecycleScope.launch {
            if (viewModel.getUserById(dataFromLogin)?.userTypeName == "Staff") {
                val menu = binding.bottomNavigationView.menu
                menu.findItem(R.id.settingsFragment).isVisible = false
            }
        }
    }

    /**
     * Adding null for remember me for logout section if user logout it will remove remember me
     * property
     */
    private fun rememberMe() {
        val sharedPref = getSharedPreferences("remember_me", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("remember_me", null)
        editor.apply()
    }
}