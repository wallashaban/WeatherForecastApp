package com.example.weatherforecastapplication.weatherFeature.view

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.databinding.FragmentHomeBinding
import com.example.weatherforecastapplication.data.local.LocalDataSourceImpl
import com.example.weatherforecastapplication.data.models.CurrentWeather
import com.example.weatherforecastapplication.data.models.FiveDaysForecast
import com.example.weatherforecastapplication.data.remote.RemoteDataSourceImpl
import com.example.weatherforecastapplication.data.models.WeatherParam
import com.example.weatherforecastapplication.settings.viewModel.SettingsViewModel
import com.example.weatherforecastapplication.data.models.WindSpeed
import com.example.weatherforecastapplication.utils.API_KEY
import com.example.weatherforecastapplication.utils.ApiState
import com.example.weatherforecastapplication.utils.Storage
import com.example.weatherforecastapplication.utils.WIND_UNIT
import com.example.weatherforecastapplication.utils.addCelsiusSign
import com.example.weatherforecastapplication.utils.checkConnectivity
import com.example.weatherforecastapplication.utils.convertToMeterPerSecond
import com.example.weatherforecastapplication.utils.convertToMilePerHour
import com.example.weatherforecastapplication.utils.enableLocationServices
import com.example.weatherforecastapplication.utils.getDateFromDateTime
import com.example.weatherforecastapplication.utils.getDayOfTheWeek
import com.example.weatherforecastapplication.utils.isLocationEnable
import com.example.weatherforecastapplication.utils.requestPermission
import com.example.weatherforecastapplication.utils.showSnackbar
import com.example.weatherforecastapplication.weatherFeature.viewModel.WeatherViewModel
import com.example.weatherforecastapplication.data.repo.WeatherRepositoryImpl
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
    private lateinit var dailyManager: LinearLayoutManager
    private lateinit var dailyWeatherAdapter: DailyWeatherAdapter

    /////
    private lateinit var currentWeather: CurrentWeather
    private var isChanged = false
    private var isTempChanged = false

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
        settingsViewModel = ViewModelProvider(
            requireActivity()
        )[SettingsViewModel::class.java]
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

        if (lat != 0F && long != 0F) {
            val weatherParam = WeatherParam(
                lat.toDouble(),
                long.toDouble(),
                API_KEY,
                Storage.getCurrentUnit(requireActivity()),
                Storage.getPreferredLocale(requireActivity())
            )
            weatherViewModel.getFiveDaysForecast(weatherParam)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        WIND_UNIT = Storage.getCurrentWindUnit(requireContext())
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        manager = LinearLayoutManager(requireContext())
        manager.orientation = LinearLayoutManager.HORIZONTAL
        hourlyWeatherAdapter = HourlyWeatherAdapter(requireContext())
        binding.hourlyWeatherRv.layoutManager = manager
        binding.hourlyWeatherRv.adapter = hourlyWeatherAdapter

        dailyManager = LinearLayoutManager(requireContext())
        dailyManager.orientation = LinearLayoutManager.VERTICAL
        dailyWeatherAdapter = DailyWeatherAdapter(requireContext())
        binding.dailyWeatherRv.layoutManager = dailyManager
        binding.dailyWeatherRv.adapter = dailyWeatherAdapter

        observeWindSpeed()
        observeTemperature()
        observeLanguage()

        val result =
            registerForActivityResult(ActivityResultContracts.RequestPermission())
            { isGranted ->
                if (isGranted) {
                    if (isLocationEnable(requireContext())) {
                        if (lat == 0F && long == 0F && weatherViewModel.wind == null)
                            getFreshLocation()
                    } else {
                        enableLocationServices(requireActivity())
                    }
                } else {
                    requestPermission(requireActivity())
                }
            }
        result.launch(ACCESS_FINE_LOCATION)
        observeWeather()
    }

    private fun observeWeather() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                weatherViewModel.fiveDaysForecast.collect { result ->
                    when (result) {
                        is ApiState.Loading -> {
                            binding.homeProgressBar.visibility = View.VISIBLE
                            binding.homeData.visibility = View.GONE
                        }

                        is ApiState.Failure -> {
                            binding.homeProgressBar.visibility = View.GONE
                            binding.homeData.visibility = View.GONE
                            Toast.makeText(
                                requireContext(),
                                result.error.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is ApiState.Success -> {
                            if (isChanged && !isTempChanged) {
                                result.data.currentDate = LocalDate.now().toString()
                                weatherViewModel.addCurrentWeather(result.data)
                                weatherViewModel.deleteCurrentWeather(
                                    Storage.getCurrentDate(
                                        requireContext()
                                    )
                                )
                                Storage.setCurrent(requireContext(), LocalDate.now().toString())
                                isChanged = !isChanged
                            }
                            binding.homeProgressBar.visibility = View.GONE
                            binding.homeData.visibility = View.VISIBLE
                            currentWeather = result.data.list[0]
                            setCurrentWeather(result)
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

    private fun observeWindSpeed() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
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
    }

    private fun observeTemperature() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                settingsViewModel.unitsSharedFlow.collect {
                    if (weatherViewModel.unit != it.name) {
                        getFreshLocation()
                        weatherViewModel.unit = it.name
                        isTempChanged = true
                    }
                }
            }
        }
    }

    private fun observeLanguage() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                settingsViewModel.langSharedFlow.collect {
                    getFreshLocation()
                    isTempChanged = true
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val search = requireActivity().findViewById<View>(R.id.search)
        search.visibility = View.VISIBLE
//        if (checkPermissions()) {
//            Log.i(TAG, "onStart: permission")
//            if (isLocationEnable(requireContext())) {
//                Log.i(TAG, "onStart: isLocation  enabled")
//                if (lat == 0F && long == 0F)
//                    getFreshLocation()
//            } else {
//                enableLocationServices(requireActivity())
//                Log.i(TAG, "onStart: enable location")
//            }
//        } else {
//            Log.i(TAG, "onStart: request location")
//           // requestPermission(requireActivity())
//        }

    } // Review

    private fun setDailyWeather(result: ApiState.Success<FiveDaysForecast>) {
        var day: String? = null
        val filteredDailyWeather = mutableListOf<CurrentWeather>()
        for (item in result.data.list) {
            if (getDayOfTheWeek(item.dateText) != day) {
                filteredDailyWeather.add(item)
            }
            day = getDayOfTheWeek(item.dateText)
        }
        filteredDailyWeather.removeAt(0)
        dailyWeatherAdapter.submitList(filteredDailyWeather)
    } // Done

    private fun setHourlyWeather(result: ApiState.Success<FiveDaysForecast>) {
        val today = LocalDate.now()
        val filteredWeather = result.data.list.filter { weather ->
            getDateFromDateTime(weather.dateText) == today.toString()
        }
        hourlyWeatherAdapter.submitList(filteredWeather)
    } // Done

    private fun setCurrentWeather(result: ApiState.Success<FiveDaysForecast>) {
        if (WIND_UNIT == "m/h" && weatherViewModel.wind == null) {
            currentWeather.wind.speed =
                convertToMilePerHour(currentWeather.wind.speed)
        }
        binding.windUnit = WIND_UNIT
        binding.tempUnit = addCelsiusSign(currentWeather.main.temp, requireContext())
        result.data.list[0].wind.speed = currentWeather.wind.speed
        binding.weather = result.data
    }

    @SuppressLint("MissingPermission")
    private fun getFreshLocation() {
        fusedClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedClient.requestLocationUpdates(
            LocationRequest.Builder(0).apply {
                setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
            }.build(),
            getLocationCallBack(),
            Looper.myLooper()
        )
    }
    private fun getLocationCallBack(): LocationCallback {
        return object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (isAdded) {
                    val locationRequest = locationResult.lastLocation
                    latitude = locationRequest?.latitude
                    longitude = locationRequest?.longitude
                    val weatherParam = WeatherParam(
                        latitude!!, longitude!!, API_KEY,
                        Storage.getCurrentUnit(requireActivity()),
                        Storage.getPreferredLocale(requireActivity())
                    )
                    getWeather(weatherParam)
                    fusedClient.removeLocationUpdates(this)
                }
            }
        }
    }
    private fun getWeather(weatherParam: WeatherParam) {
        if (Storage.getCurrentDate(requireContext()) == LocalDate.now()
                .toString() && !isTempChanged
        ) {
            Log.i(TAG, "onLocationResult: getCurrentLocation")
            weatherViewModel.getCurrentWeatherUsingRoom()
        } else {
            if (checkConnectivity(requireContext())) {
                weatherViewModel.getFiveDaysForecast(weatherParam)
                isChanged = true
                Log.i(TAG, "onLocationResult: Changed")
            } else {
                showSnackbar(requireActivity(), getString(R.string.noInternetMessage))
            }
        }
    }
    private fun checkPermissions(): Boolean {
        var result = false
        if ((requireActivity().checkSelfPermission(
                ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
            ||
            (requireActivity().checkSelfPermission(
                ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
        ) {
            result = true
        }
        return result
    } // Review
}