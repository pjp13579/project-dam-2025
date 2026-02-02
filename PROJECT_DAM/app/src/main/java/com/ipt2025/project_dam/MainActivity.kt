// MainActivity.kt
package com.ipt2025.project_dam

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.navigateUp
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ipt2025.project_dam.data.TokenManager
import com.ipt2025.project_dam.data.api.RetrofitProvider
import com.ipt2025.project_dam.databinding.FragmentActivityMainHolderBinding

/**
 * app entry point. acts as a container that holds the views for other fragments
 * handles the bottom nav (logout and qrcode) and the top toolbar (back button) actions and visibility
 */
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: FragmentActivityMainHolderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        // boilerplate app configurations
        super.onCreate(savedInstanceState)
        // inflate the layout and set the root view
        binding = FragmentActivityMainHolderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // use the Toolbar defined in XML as the Activity's Action Bar
        setSupportActionBar(binding.toolbar)

        // initialize Navigation: link the NavHostFragment to the NavController
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.main_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        /**
         * yop-Level Destinations:
         * views listed here won't show a "back" arrow in the toolbar.
         * these are the main landing screens (dDashboard and login).
         */
        val topLevelDestinations = setOf(R.id.dashboard, R.id.loginFragment)
        appBarConfiguration = AppBarConfiguration(topLevelDestinations)

        // connect the ActionBar to Navigation so the title updates automatically
        setupActionBarWithNavController(navController, appBarConfiguration)

        // initialize Bottom Navigation and clear default selection states
        var bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.menu.setGroupCheckable(0, true, false)
        for (i in 0 until bottomNav.menu.size()) {
            bottomNav.menu.getItem(i).isChecked = false
        }

        /**
         * run every time we navigate to another view
         * listener to decide if we should hide/show the Bottom Bar depending on the view
         */
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                // hide bottom bar for login, qr scan and details
                R.id.loginFragment,
                R.id.nav_qr,
                R.id.deviceDetailsFragment -> {
                    bottomNav.visibility = View.GONE
                }
                else -> {
                    // show bottom bar everywhere else
                    bottomNav.visibility = View.VISIBLE
                }
            }
        }

        val tokenManager = TokenManager(this)

        // handles navigation for bottom bar clicks
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
                    // otherwise, navigate to start destination
                    navController.popBackStack(R.id.loginFragment, false)
                }
            }
        }
    }

    /**
     * handles the top left back arrow in the action bar
     * set the android NavController responsible for the button back navigation
     */
    override fun onSupportNavigateUp(): Boolean {
        val navController = (supportFragmentManager
            .findFragmentById(R.id.main_fragment_container) as NavHostFragment)
            .navController
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}