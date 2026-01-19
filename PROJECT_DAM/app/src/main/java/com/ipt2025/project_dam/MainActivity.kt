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

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
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

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment,
                R.id.nav_qr,
                R.id.deviceDetailsFragment -> {
                    bottomNav.visibility = View.GONE
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    supportActionBar?.setDisplayShowHomeEnabled(true)
                }
                else -> {
                    bottomNav.visibility = View.VISIBLE
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    supportActionBar?.setDisplayShowHomeEnabled(false)
                }
            }
        }

        val tokenManager = TokenManager(this)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_qr -> {
                    navController.navigate(R.id.action_dashboard_to_QRCodeView)
                    true
                }
                R.id.nav_logout -> {
                    RetrofitProvider.clearToken()
                    tokenManager.clearToken()
                    navController.popBackStack(R.id.loginFragment, inclusive = false)
                    true
                }
                else -> false
            }
        }

        // Handle back button press - FIXED
        onBackPressedDispatcher.addCallback(this) {
            val navController = (supportFragmentManager
                .findFragmentById(R.id.main_fragment_container) as NavHostFragment)
                .navController

            // Try to navigate up first
            if (!navController.navigateUp()) {
                // If navigateUp returns false, it means we're at the start destination
                // Only finish the activity if we're at loginFragment (start destination)
                if (navController.currentDestination?.id == R.id.loginFragment) {
                    finish()
                } else {
                    // Otherwise, navigate to start destination
                    navController.popBackStack(R.id.loginFragment, false)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = (supportFragmentManager
            .findFragmentById(R.id.main_fragment_container) as NavHostFragment)
            .navController
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}