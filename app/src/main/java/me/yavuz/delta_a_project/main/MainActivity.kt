package me.yavuz.delta_a_project.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import me.yavuz.delta_a_project.R
import me.yavuz.delta_a_project.database.DbHelper
import me.yavuz.delta_a_project.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DbHelper
    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        dbHelper = DbHelper(this)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.mainFrame) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        dbHelper.close()
    }

    /*
    navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)
        homeParentViewModel= ViewModelProvider(this)[HomeParentViewModel::class.java]
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (controller.currentDestination?.id == R.id.home2) {
                binding.homeParentIconsLinear.visibility = View.VISIBLE
                MainActivity.viewPagerEnable.value = true
            } else {
                binding.homeParentIconsLinear.visibility = View.GONE
                MainActivity.viewPagerEnable.value = false
            }
        }

        private fun replaceFragment(fragment: Fragment) {

        val fragmentManager = supportFragmentManager
        val replaceTransaction = fragmentManager.beginTransaction()
        replaceTransaction.replace(R.id.mainFrame, fragment)
        replaceTransaction.commit()
    }
        */

}