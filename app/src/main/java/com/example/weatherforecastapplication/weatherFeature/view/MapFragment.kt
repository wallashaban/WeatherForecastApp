package com.example.weatherforecastapplication.weatherFeature.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import com.example.weatherforecastapplication.databinding.FragmentMapBinding
import com.example.weatherforecastapplication.network.RemoteDataSourceImpl
import com.example.weatherforecastapplication.weatherFeature.viewModel.WeatherViewModel
import com.example.weatherforecastapplication.weatherFeature.viewModel.WeatherViewModelFactory
import com.example.weatherforecastapplication.weatherRepository.WeatherRepositoryImpl
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapFragment : Fragment() ,OnMapReadyCallback,GoogleMap.OnMapClickListener{

    private lateinit var binding: FragmentMapBinding
    private lateinit var googleMap: GoogleMap
    private val PLACE_PICKER_REQUEST = 1
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var weatherViewModelFactory: WeatherViewModelFactory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weatherViewModelFactory = WeatherViewModelFactory(
            WeatherRepositoryImpl.getInstance(RemoteDataSourceImpl)
        )
        weatherViewModel = ViewModelProvider(this, weatherViewModelFactory)
            .get(WeatherViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.onResume()
        binding.mapView.getMapAsync(this)

    }

    override fun onMapReady(map: GoogleMap) {
            this.googleMap = map
        googleMap.setOnMapClickListener(this)


    }

    override fun onMapClick(latLng: LatLng) {
        googleMap.clear()
        val markerOptions = MarkerOptions()
            .position(latLng)
            .title("Selected Place")
        googleMap.addMarker(markerOptions)
        val latitude = latLng.latitude
        val longitude = latLng.longitude
        val action= MapFragmentDirections.actionMapFragmentToHomeFragment(
            latitude.toFloat(),longitude.toFloat())
       Navigation.findNavController(requireView()).navigate(action)
//        weatherViewModel.getCurrentWeather(WeatherParam(
//            latitude,longitude, API_KEY, getCurrentUnit(requireContext()),
//            getCurrentLang(requireContext())
//        ))
    }
}