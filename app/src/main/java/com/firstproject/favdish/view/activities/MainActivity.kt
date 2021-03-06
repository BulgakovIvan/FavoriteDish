package com.firstproject.favdish.view.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.firstproject.favdish.R
import com.firstproject.favdish.databinding.ActivityMainBinding
import com.firstproject.favdish.model.notification.NotifyWorker
import com.firstproject.favdish.utils.NOTIFICATION_ID
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_all_dishes, R.id.navigation_favorite_dishes,
                R.id.navigation_random_dish, R.id.navigation_addUpdate
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        binding.fab.setOnClickListener {
            hideBottomNavigationView(R.id.navigation_addUpdate)
        }

        if (intent.hasExtra(NOTIFICATION_ID)) {
            val notificaitonId = intent.getIntExtra(NOTIFICATION_ID, 0)
            binding.navView.selectedItemId = R.id.navigation_random_dish
        }

        startWork()
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }

    fun hideBottomNavigationView(directions: NavDirections) {
        binding.navView.clearAnimation()
        binding.fab.clearAnimation()

        binding.fab.animate().alpha(0f).duration = 300

        ObjectAnimator.ofFloat(binding.navView, "translationY",
            binding.navView.height.toFloat()).apply {

            duration = 300
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    binding.navView.visibility = View.GONE
                    navController.navigate(directions)
                }
            })
            start()
        }
    }

    fun hideBottomNavigationView(directions: Int) {
        binding.navView.clearAnimation()
        binding.fab.clearAnimation()

        binding.fab.animate().alpha(0f).duration = 300

        ObjectAnimator.ofFloat(binding.navView, "translationY",
            binding.navView.height.toFloat()).apply {

            duration = 300
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    binding.navView.visibility = View.GONE
                    navController.navigate(directions)
                }
            })
            start()
        }
    }

    fun showBottomNavigationView() {
        if (binding.navView.visibility == View.GONE) {
            binding.navView.visibility = View.VISIBLE
            binding.navView.clearAnimation()
            binding.navView.animate().translationY(0f).duration = 300

            binding.fab.clearAnimation()
            binding.fab.animate().alpha(0.75f).duration = 300
        }
    }

    private fun createConstraints() = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
        .setRequiresCharging(false)
        .setRequiresBatteryNotLow(true)
        .build()

    private fun createWorkRequest() =
        PeriodicWorkRequestBuilder<NotifyWorker>(15, TimeUnit.MINUTES)
            .setConstraints(createConstraints())
            .build()

    private fun startWork() {
        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "FavDish Notify Work",
                ExistingPeriodicWorkPolicy.KEEP,
                createWorkRequest()
            )
    }
}