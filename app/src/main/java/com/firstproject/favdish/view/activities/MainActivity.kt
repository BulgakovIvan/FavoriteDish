package com.firstproject.favdish.view.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.firstproject.favdish.R
import com.firstproject.favdish.databinding.ActivityMainBinding
import com.firstproject.favdish.utils.TAG
import com.firstproject.favdish.view.fragments.AddUpdateFragment

class MainActivity : AppCompatActivity(), AddUpdateFragment.ChangeActivity {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard,
                R.id.navigation_notifications, R.id.navigation_addUpdate
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        binding.fab.setOnClickListener {
            navController.navigate(R.id.navigation_addUpdate)
        }
    }

    override fun hideMenu(isVisible: Boolean) {
        if (isVisible) {
            binding.navView.visibility = View.VISIBLE
            binding.fab.visibility = View.VISIBLE
        } else {
            binding.navView.visibility = View.GONE
            binding.fab.visibility = View.GONE
        }
    }
}