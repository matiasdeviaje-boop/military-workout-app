package com.militaryworkout.app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.militaryworkout.app.R
import com.militaryworkout.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar Navigation
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        // Configurar Bottom Navigation
        val bottomNav = binding.bottomNavigation
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.nav_workout -> {
                    navController.navigate(R.id.workoutFragment)
                    true
                }
                R.id.nav_progress -> {
                    navController.navigate(R.id.progressFragment)
                    true
                }
                R.id.nav_settings -> {
                    navController.navigate(R.id.settingsFragment)
                    true
                }
                else -> false
            }
        }
    }
}

