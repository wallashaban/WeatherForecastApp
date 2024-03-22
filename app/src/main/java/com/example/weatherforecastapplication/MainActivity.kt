package com.example.weatherforecastapplication

import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.weatherforecastapplication.databinding.ActivityMainBinding
import com.example.weatherforecastapplication.shared.LocaleUtil
import com.example.weatherforecastapplication.shared.Storage
import com.example.weatherforecastapplication.shared.applyMode

private const val TAG = "MainActivity"
class MainActivity : BaseActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyMode(applicationContext)
        LocaleUtil.applyLocalizedContext(this, Storage.getPreferredLocale(this))
       binding. menu.setOnClickListener {
            if (binding.drawer .isDrawerOpen(GravityCompat.START)) {
                binding.drawer.closeDrawer(GravityCompat.START)
            } else {
                binding.drawer.openDrawer(GravityCompat.START)
            }
        }
        navController =
            Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(binding.navigationView, navController)
        binding.search.setOnClickListener {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            val destinationFragmentId = R.id.mapFragment
            navController.navigate(destinationFragmentId)
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp() || navController.navigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (binding.drawer.isDrawerOpen(GravityCompat.START)) {
                binding.drawer.closeDrawer(GravityCompat.START)
            } else {
                binding.drawer.openDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }


}