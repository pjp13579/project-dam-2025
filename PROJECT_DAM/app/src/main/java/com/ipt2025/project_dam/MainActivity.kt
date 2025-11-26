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
                // Screens where you want the bottom bar HIDDEN
                R.id.loginFragment-> {
                    bottomNav.visibility = View.GONE
                }

                // Screens where it should be visible
                else -> {
                    bottomNav.visibility = View.VISIBLE
                }
            }
        }

        val tokenManager = TokenManager(this)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_qr -> {

                    true
                }
                R.id.nav_logout -> {
                    RetrofitProvider.clearToken()
                    tokenManager.clearToken()
                    navController.popBackStack(R.id.loginFragment, inclusive = false )
                    true
                }
                else -> false
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