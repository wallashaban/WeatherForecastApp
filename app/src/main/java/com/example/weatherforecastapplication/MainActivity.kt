package com.example.weatherforecastapplication

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.weatherforecastapplication.shared.LocaleHelper
import com.example.weatherforecastapplication.shared.LocaleUtil
import com.example.weatherforecastapplication.shared.applyLang
import com.example.weatherforecastapplication.shared.applyMode
import com.example.weatherforecastapplication.shared.getCurrentLang
import com.google.android.material.navigation.NavigationView
import kotlin.math.log

class MainActivity : BaseActivity() {

    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var menu: ImageView

    init {
       // applyMode(applicationContext)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
        applyMode(applicationContext)
       LocaleUtil.applyLocalizedContext(this, getCurrentLang(this))
        Log.i("TAG", "onCreate: ")
        setContentView(R.layout.activity_main)
        menu = findViewById(R.id.menu)
        drawerLayout = findViewById<DrawerLayout>(R.id.drawer)
        navigationView = findViewById(R.id.navigation_view)
        menu.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        navController =
            Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(navigationView, navController)
    }


    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp() || navController.navigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        /*applyLang(this)
        applyMode(this)*/
    }
}