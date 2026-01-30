// MainActivity.kt
package com.ipt2025.project_dam

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.navigateUp
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ipt2025.project_dam.data.TokenManager
import com.ipt2025.project_dam.data.api.RetrofitProvider
import com.ipt2025.project_dam.databinding.ActivityMainBinding

/**
 * main entry point. handles the bottom nav and the top toolbar
 */
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        // boilerplate app configurations
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.main_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        val topLevelDestinations = setOf(R.id.dashboard, R.id.loginFragment)
        appBarConfiguration = AppBarConfiguration(topLevelDestinations)
        setupActionBarWithNavController(navController, appBarConfiguration)

        var bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.menu.setGroupCheckable(0, true, false)
        for (i in 0 until bottomNav.menu.size()) {
            bottomNav.menu.getItem(i).isChecked = false
        }

        // listener to decide if we should show the bottom bar or not
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                // hide bottom bar for login, qr scan and details
                R.id.loginFragment,
                R.id.nav_qr,
                R.id.deviceDetailsFragment -> {
                    bottomNav.visibility = View.GONE
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    supportActionBar?.setDisplayShowHomeEnabled(true)
                }
                else -> {
                    // show bottom bar everywhere else
                    bottomNav.visibility = View.VISIBLE
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    supportActionBar?.setDisplayShowHomeEnabled(false)
                }
            }
        }

        val tokenManager = TokenManager(this)

        // handle bottom nav clicks
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_qr -> {
                    // qr code. navigate to qr code fragment
                    navController.navigate(R.id.action_to_QRCodeView)
                    true
                }
                R.id.nav_logout -> {
                    // logout. wipe the persisted token and navigate user back to login
                    RetrofitProvider.clearToken()
                    tokenManager.clearToken()
                    navController.popBackStack(R.id.loginFragment, inclusive = false)
                    true
                }
                else -> false
            }
        }

            // custom back button logic so we don't close the app when back button is pressed
            onBackPressedDispatcher.addCallback(this) {
            val navController = (supportFragmentManager
                .findFragmentById(R.id.main_fragment_container) as NavHostFragment)
                .navController

            // try to navigate up first
            if (!navController.navigateUp()) {
                // if navigateUp returns false, it means we're at the start destination
                // only finish the activity if we're at loginFragment (start destination)
                if (navController.currentDestination?.id == R.id.loginFragment) {
                    finish()
                } else {
                    // Otherwise, navigate to start destination
                    navController.popBackStack(R.id.loginFragment, false)
                }
            }
        }
    }

    // handles the top left back arrow in the action bar
    override fun onSupportNavigateUp(): Boolean {
        val navController = (supportFragmentManager
            .findFragmentById(R.id.main_fragment_container) as NavHostFragment)
            .navController
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}