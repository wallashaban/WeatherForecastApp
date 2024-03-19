package com.example.weatherforecastapplication

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.weatherforecastapplication.favouritesFeature.model.LocalDataSourceImpl
import com.example.weatherforecastapplication.network.RemoteDataSourceImpl
import com.example.weatherforecastapplication.network.WeatherParam
import com.example.weatherforecastapplication.shared.API_KEY
import com.example.weatherforecastapplication.shared.LocaleHelper
import com.example.weatherforecastapplication.shared.LocaleUtil
import com.example.weatherforecastapplication.shared.applyLang
import com.example.weatherforecastapplication.shared.applyMode
import com.example.weatherforecastapplication.shared.getCurrentLang
import com.example.weatherforecastapplication.shared.getCurrentUnit
import com.example.weatherforecastapplication.weatherFeature.viewModel.WeatherViewModel
import com.example.weatherforecastapplication.weatherFeature.viewModel.WeatherViewModelFactory
import com.example.weatherforecastapplication.weatherRepository.WeatherRepositoryImpl
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.material.navigation.NavigationView
import kotlin.math.log

private const val TAG = "MainActivity"
class MainActivity : BaseActivity() {

    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var menu: ImageView
    private lateinit var search:ImageView
    // view model
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var weatherViewModelFactory: WeatherViewModelFactory
    //Location
    private lateinit var fusedClient: FusedLocationProviderClient
    private var latitude: Double? = 0.0
    private var longitude: Double? = 0.0
    val REQUEST_LOCATION_CODE = 5005
    var lat: Float = 0F
    var long: Float = 0F


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        weatherViewModelFactory = WeatherViewModelFactory(
            WeatherRepositoryImpl.getInstance(
                RemoteDataSourceImpl,
                LocalDataSourceImpl.getInstance(this)
            )
        )
        weatherViewModel = ViewModelProvider(this, weatherViewModelFactory)
            .get(WeatherViewModel::class.java)
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
        search = findViewById(R.id.search)
        search.setOnClickListener {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            val destinationFragmentId = R.id.mapFragment
            navController.navigate(destinationFragmentId)
        }



    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.i("TAG", "onRequestPermissionsResult: ")
        if (requestCode == REQUEST_LOCATION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getFreshLocation()
            }
        }
    }

    private fun requestLocation() {
        Log.i("TAG", "requestLocation: ")
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            REQUEST_LOCATION_CODE
        )
        getFreshLocation()
    }

    private fun enableLocationServices() {
        Toast.makeText(this, "Turn on location", Toast.LENGTH_SHORT).show()
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }



    private fun isLocationEnable(): Boolean {
        val locationManager: LocationManager =
            this.getSystemService(LOCATION_SERVICE)
                    as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    private fun getFreshLocation() {
        val locationCallBack = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                //if (isAdded) {
                    val locationRequest = locationResult.lastLocation
                    latitude = locationRequest?.latitude
                    longitude = locationRequest?.longitude
                    val weatherParam = WeatherParam(
                        latitude!!, longitude!!, API_KEY,
                        getCurrentUnit(this@MainActivity),
                        getCurrentLang(this@MainActivity)
                    )
                    weatherViewModel.getCurrentWeather(weatherParam)
                    weatherViewModel.getFiveDaysForecast(weatherParam)
                    fusedClient.removeLocationUpdates(this)

               // }
            }
        }
        fusedClient = LocationServices.getFusedLocationProviderClient(this)
        fusedClient.requestLocationUpdates(
            LocationRequest.Builder(0).apply {
                setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
            }.build(),
            locationCallBack,
            Looper.myLooper()
        )
    }


    private fun checkPermissions(): Boolean {
        var result = false
        if ((ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
            ||
            (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
        ) {
            result = true
        }
        return result
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

        Log.i("TAG", "onStart: ")
        if (checkPermissions()) {
            Log.i("TAG", "onStart: permission")
            if (isLocationEnable()) {
                if (lat == 0F && long == 0F)
                    getFreshLocation()
            } else
                enableLocationServices()
        } else {
            Log.i("TAG", "onStart: request location")
            requestLocation()
        }
    }

}