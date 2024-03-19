package com.example.weatherforecastapplication.weatherFeature.view

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context.*
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.text.Spannable
import android.text.SpannableString
import android.text.style.SuperscriptSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.weatherforecastapplication.MainActivity
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.databinding.FragmentHomeBinding
import com.example.weatherforecastapplication.favouritesFeature.model.LocalDataSourceImpl
import com.example.weatherforecastapplication.model.CurrentWeather
import com.example.weatherforecastapplication.network.RemoteDataSourceImpl
import com.example.weatherforecastapplication.network.WeatherParam
import com.example.weatherforecastapplication.shared.API_KEY
import com.example.weatherforecastapplication.shared.getCurrentLang
import com.example.weatherforecastapplication.shared.getCurrentLocation
import com.example.weatherforecastapplication.shared.getCurrentUnit
import com.example.weatherforecastapplication.shared.getCurrentWindUnit
import com.example.weatherforecastapplication.shared.getDateFromDateTime
import com.example.weatherforecastapplication.shared.getDayOfTheWeek
import com.example.weatherforecastapplication.shared.saveSelectedLocatioToSharedPref
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
import java.time.LocalDate

private const val TAG = "HomeFragment"

class HomeFragment : Fragment() {
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var weatherViewModelFactory: WeatherViewModelFactory
    private lateinit var binding: FragmentHomeBinding
    private lateinit var manager: LinearLayoutManager
    private lateinit var hourlyWeatherAdapter: HourlyWeatherAdapter
    private lateinit var dailyanager: LinearLayoutManager
    private lateinit var dailyWeatherAdapter: DailyWeatherAdapter

    //Location
    private lateinit var fusedClient: FusedLocationProviderClient
    private var latitude: Double? = 0.0
    private var longitude: Double? = 0.0
    val REQUEST_LOCATION_CODE = 5005
    var lat: Float = 0F
    var long: Float = 0F
    private val ERROR_DIALOG_REQUEST = 5005

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        weatherViewModelFactory = WeatherViewModelFactory(
            WeatherRepositoryImpl.getInstance(
                RemoteDataSourceImpl,
                LocalDataSourceImpl.getInstance(requireContext())
            )
        )
        weatherViewModel = ViewModelProvider(requireActivity(), weatherViewModelFactory)
            .get(WeatherViewModel::class.java)
        lat = arguments?.getFloat("latitude") ?: 0F
        long = arguments?.getFloat("longitude") ?: 0F

        if (lat != 0F && long != 0F) {
            val weatherParam = WeatherParam(
                lat.toDouble(), long.toDouble(), API_KEY,
                getCurrentUnit(requireActivity()),
                getCurrentLang(requireActivity())
            )
            weatherViewModel.getCurrentWeather(weatherParam)
            weatherViewModel.getFiveDaysForecast(weatherParam)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        if(getCurrentLocation(requireContext())=="map")
//        {
//            val action =
//                HomeFragmentDirections.actionHomeFragmentToMapFragment()
//            Navigation.findNavController(requireView()).navigate(action)
//        }

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

        weatherViewModel.fiveDaysForecast.observe(requireActivity()) {
            val today = LocalDate.now()
            val filteredWeather = it.list.filter { weather ->
                getDateFromDateTime(weather.dateText!!) == today.toString()
            }

            hourlyWeatherAdapter.submitList(filteredWeather)

            var day: String? = null
            val filteredDailyWeather = mutableListOf<CurrentWeather>()
            for (item in it.list) {
                if (getDayOfTheWeek(item.dateText!!) != day) {
                    filteredDailyWeather.add(item)
                }

                day = getDayOfTheWeek(item.dateText)
            }

            filteredDailyWeather.removeAt(0)
            dailyWeatherAdapter.submitList(filteredDailyWeather)
        }
        weatherViewModel.currentWeather.observe(requireActivity()) {
            var unit = "m/s"
            if (getCurrentWindUnit(requireContext()) != "Meter/Sec") {
                // val speed =  it.wind.speed * 2.23694
                //it.wind.speed = speed
                //  "%.2f".format(speed).toDouble()
                unit = "m/h"
            }
            binding.unit = unit
            binding.weather = it
            Log.i("TAG", "onViewCreated: ${it.clouds.all}")
            /* Glide.with(view.context)
                .load("https://openweathermap.org/img/wn/${it.weather.get(0).icon}.png")
                .apply(
                    RequestOptions().override(200, 200)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_background)
                )
                .into(binding.imageView)
          val unit =  if (getCurrentUnit(requireContext())=="metric")
            "C"
            else
            ""
            binding.cityName.text = it.name
            val spannableString = SpannableString("${it.main.temp}\u00B0$unit")
            spannableString.setSpan(
                SuperscriptSpan(), it.main.temp.toString().length,
                spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            binding.temp.text = spannableString//"${it.main.temp}°C"
            binding.weatherStatus.text = it.weather[0].description*/
            weatherViewModel.saveCurrentWeatherToRoom(it)
        }

    }

    init {
        isServicesOK()
    }

    fun isServicesOK(): Boolean {
        Log.d(TAG, "isServicesOK: checking google services version")
        if (isAdded) {
            val available =
                GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(requireContext())
            if (available == ConnectionResult.SUCCESS) {
                //everything is fine and the user can make map requests
                Log.d(TAG, "isServicesOK: Google Play Services is working")
                return true
            } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
                //an error occured but we can resolve it
                Log.d(TAG, "isServicesOK: an error occured but we can fix it")

                val dialog: Dialog = GoogleApiAvailability.getInstance()
                    .getErrorDialog(requireActivity(), available, ERROR_DIALOG_REQUEST)!!
                dialog.show()
            } else {
                Toast.makeText(requireContext(), "You can't make map requests", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        return false
    }

    override fun onStart() {
        super.onStart()
        val search = requireActivity().findViewById<View>(R.id.search)
        search.visibility = View.VISIBLE
        // applyLang(requireContext())
        //applyMode(requireContext())
//        Log.i("TAG", "onStart: ")
//        if (checkPermissions()) {
//            Log.i("TAG", "onStart: permission")
//            if (isLocationEnable()) {
//                if (lat == 0F && long == 0F)
//                    getFreshLocation()
//            } else
//                enableLocationServices()
//        } else {
//            Log.i("TAG", "onStart: request location")
//            requestLocation()
//        }
    }


    private fun requestLocation() {
        Log.i("TAG", "requestLocation: ")
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                ACCESS_COARSE_LOCATION,
                ACCESS_FINE_LOCATION
            ),
            REQUEST_LOCATION_CODE
        )
        getFreshLocation()
    }

    private fun enableLocationServices() {
        Toast.makeText(requireContext(), "Turn on location", Toast.LENGTH_SHORT).show()
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    private fun isLocationEnable(): Boolean {
        val locationManager: LocationManager =
            requireContext().getSystemService(LOCATION_SERVICE)
                    as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    private fun getFreshLocation() {
        val locationCallBack = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (isAdded) {
                    val locationRequest = locationResult.lastLocation
                    latitude = locationRequest?.latitude
                    longitude = locationRequest?.longitude
                    val weatherParam = WeatherParam(
                        latitude!!, longitude!!, API_KEY,
                        getCurrentUnit(requireActivity()),
                        getCurrentLang(requireActivity())
                    )
                    weatherViewModel.getCurrentWeather(weatherParam)
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