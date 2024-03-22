package com.example.weatherforecastapplication.weatherFeature.view

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.SuperscriptSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.databinding.FragmentHomeBinding
import com.example.weatherforecastapplication.favouritesFeature.model.LocalDataSourceImpl
import com.example.weatherforecastapplication.model.CurrentWeather
import com.example.weatherforecastapplication.model.FiveDaysForecast
import com.example.weatherforecastapplication.network.RemoteDataSourceImpl
import com.example.weatherforecastapplication.network.WeatherParam
import com.example.weatherforecastapplication.settings.viewModel.SettingsViewModel
import com.example.weatherforecastapplication.settings.viewModel.WindSpeed
import com.example.weatherforecastapplication.shared.API_KEY
import com.example.weatherforecastapplication.shared.ApiState
import com.example.weatherforecastapplication.shared.REQUEST_LOCATION_CODE
import com.example.weatherforecastapplication.shared.Storage
import com.example.weatherforecastapplication.shared.WIND_UNIT
import com.example.weatherforecastapplication.shared.addCelsiusSign
import com.example.weatherforecastapplication.shared.convertToMeterPerSecond
import com.example.weatherforecastapplication.shared.convertToMilePerHour
import com.example.weatherforecastapplication.shared.enableLocationServices
import com.example.weatherforecastapplication.shared.getDateFromDateTime
import com.example.weatherforecastapplication.shared.getDayOfTheWeek
import com.example.weatherforecastapplication.shared.isLocationEnable
import com.example.weatherforecastapplication.shared.requestLocation
import com.example.weatherforecastapplication.weatherFeature.viewModel.WeatherViewModel
import com.example.weatherforecastapplication.weatherRepository.WeatherRepositoryImpl
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.launch
import java.time.LocalDate

private const val TAG = "HomeFragment"

class HomeFragment : Fragment() {
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var weatherViewModelFactory: WeatherViewModel.Factory
    private lateinit var binding: FragmentHomeBinding
    private lateinit var manager: LinearLayoutManager
    private lateinit var hourlyWeatherAdapter: HourlyWeatherAdapter
    private lateinit var dailyanager: LinearLayoutManager
    private lateinit var dailyWeatherAdapter: DailyWeatherAdapter

    /////
    private lateinit var currentWeather: CurrentWeather

    // settings 
    private lateinit var settingsViewModel: SettingsViewModel

    //Location
    private lateinit var fusedClient: FusedLocationProviderClient
    private var latitude: Double? = 0.0
    private var longitude: Double? = 0.0
    private var lat: Float = 0F
    private var long: Float = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: ")
        settingsViewModel = ViewModelProvider(requireActivity())[SettingsViewModel::class.java]
        weatherViewModelFactory = WeatherViewModel.Factory(
            WeatherRepositoryImpl.getInstance(
                RemoteDataSourceImpl,
                LocalDataSourceImpl.getInstance(requireContext())
            )
        )
        weatherViewModel = ViewModelProvider(
            requireActivity(),
            weatherViewModelFactory
        )[WeatherViewModel::class.java]
        lat = arguments?.getFloat("latitude") ?: 0F
        long = arguments?.getFloat("longitude") ?: 0F

        //review
        if (lat != 0F && long != 0F) {
            val weatherParam = WeatherParam(
                lat.toDouble(),
                long.toDouble(),
                API_KEY,
                Storage.getCurrentUnit(requireActivity()),
                Storage.getPreferredLocale(requireActivity())
            )
            weatherViewModel.getFiveDaysForecast(weatherParam)
            // popMapFragmentFromTheBackStack(requireView())
            Log.i(TAG, "onCreate: inside if ")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.i(TAG, "onCreateView: $lat $long")

        WIND_UNIT = Storage.getCurrentWindUnit(requireContext())
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated: ")
        manager = LinearLayoutManager(requireContext())
        manager.orientation = LinearLayoutManager.HORIZONTAL
        hourlyWeatherAdapter = HourlyWeatherAdapter(requireContext())
        binding.hourlyWeatherRv.layoutManager = manager
        binding.hourlyWeatherRv.adapter = hourlyWeatherAdapter

        dailyanager = LinearLayoutManager(requireContext())
        dailyanager.orientation = LinearLayoutManager.VERTICAL
        dailyWeatherAdapter = DailyWeatherAdapter(requireContext())
        binding.dailyWeatherRv.layoutManager = dailyanager
        binding.dailyWeatherRv.adapter = dailyWeatherAdapter

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                Log.i(TAG, "onViewCreated: Scope")
                settingsViewModel.windSpeedSharedFlow.collect { wind ->
                    if (weatherViewModel.wind != WIND_UNIT) {
                        WIND_UNIT = if (wind.name == WindSpeed.MILE_PER_HOUR.toString()) {

                            currentWeather.wind.speed =
                                convertToMilePerHour(currentWeather.wind.speed)
                            "m/h"
                        } else {
                            currentWeather.wind.speed =
                                convertToMeterPerSecond(currentWeather.wind.speed)
                            "m/s"
                        }
                        weatherViewModel.wind = WIND_UNIT
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                settingsViewModel.unitsSharedFlow.collect {
                    Log.i(TAG, "onViewCreated: Scope inner ${it.name.lowercase()}")
                    if (weatherViewModel.unit != it.name) {
                        val weatherParam = WeatherParam(
                            lat.toDouble(), long.toDouble(), API_KEY,
                            it.name.lowercase(),
                            Storage.getPreferredLocale(requireActivity())
                        )
                        weatherViewModel.getFiveDaysForecast(weatherParam)
                        weatherViewModel.unit = it.name
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                weatherViewModel.fiveDaysForecast.collect { result ->
                    when (result) {
                        is ApiState.Loading -> {
                            binding.homeProgressBar.visibility=View.VISIBLE
                            binding.homeData.visibility = View.GONE
                        }

                        is ApiState.Failure -> {
                            binding.homeProgressBar.visibility=View.GONE
                            binding.homeData.visibility = View.GONE
                            Toast.makeText(
                                requireContext(),
                                result.error.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is ApiState.Success -> {
                            binding.homeProgressBar.visibility=View.GONE
                            binding.homeData.visibility = View.VISIBLE
                            currentWeather = result.data.list[0]
                            setCurrentWeather(result)
                            // weatherViewModel.saveCurrentWeatherToRoom(currentWeather)
                            // hourly weather
                            setHourlyWeather(result)
                            // daily weather
                            setDailyWeather(result)
                        }
                    }
                }
            }
        }
    }

    private fun setDailyWeather(result: ApiState.Success<FiveDaysForecast>) {
        var day: String? = null
        val filteredDailyWeather = mutableListOf<CurrentWeather>()
        for (item in result.data.list) {
            if (getDayOfTheWeek(item.dateText!!) != day) {
                filteredDailyWeather.add(item)
            }
            day = getDayOfTheWeek(item.dateText)
        }
        filteredDailyWeather.removeAt(0)
        dailyWeatherAdapter.submitList(filteredDailyWeather)
    }

    private fun setHourlyWeather(result: ApiState.Success<FiveDaysForecast>) {
        val today = LocalDate.now()
        val filteredWeather = result.data.list.filter { weather ->
            getDateFromDateTime(weather.dateText!!) == today.toString()
        }
        hourlyWeatherAdapter.submitList(filteredWeather)
    }

    private fun setCurrentWeather(result: ApiState.Success<FiveDaysForecast>) {
        if (WIND_UNIT == "m/h" && weatherViewModel.wind == null) {
            currentWeather.wind.speed =
                convertToMilePerHour(currentWeather.wind.speed)
        }
        binding.windUnit = WIND_UNIT
        binding.tempUnit = addCelsiusSign(currentWeather.main.temp,requireContext())
        binding.weather = result.data
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart: ")
        val search = requireActivity().findViewById<View>(R.id.search)
        search.visibility = View.VISIBLE
        if (checkPermissions()) {
            Log.i(TAG, "onStart: permission")
            if (isLocationEnable(requireContext())) {
                Log.i(TAG, "onStart: isLocation  enabled")
                if (lat == 0F && long == 0F)
                    getFreshLocation()
            } else {
                enableLocationServices(requireActivity())
                Log.i(TAG, "onStart: enable location")
            }
        } else {
            Log.i(TAG, "onStart: request location")
            requestLocation(requireActivity())
        }
        Log.i(TAG, "onStart: End")
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.i(TAG, "onRequestPermissionsResult: ")
        if (requestCode == REQUEST_LOCATION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getFreshLocation()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy: ")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(TAG, "onDestroyView: ")
    }

    override fun onDetach() {
        super.onDetach()
        Log.i(TAG, "onDetach: ")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause: ")
    }


    @SuppressLint("MissingPermission")
    private fun getFreshLocation() {
        val locationCallBack = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (isAdded) {
                    Log.i(TAG, "onLocationResult: ")
                    val locationRequest = locationResult.lastLocation
                    latitude = locationRequest?.latitude
                    longitude = locationRequest?.longitude
                    val weatherParam = WeatherParam(
                        latitude!!, longitude!!, API_KEY,
                        Storage.getCurrentUnit(requireActivity()),
                        Storage.getPreferredLocale(requireActivity())
                    )
                    weatherViewModel.getFiveDaysForecast(weatherParam)
                    fusedClient.removeLocationUpdates(this)
                }
            }
        }
        fusedClient = LocationServices.getFusedLocationProviderClient(requireActivity())
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
                requireContext(),
                ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
            ||
            (ContextCompat.checkSelfPermission(
                requireContext(),
                ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
        ) {
            result = true
        }
        return result
    }
}