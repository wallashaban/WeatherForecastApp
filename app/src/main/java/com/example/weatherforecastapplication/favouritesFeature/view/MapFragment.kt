package com.example.weatherforecastapplication.favouritesFeature.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.data.local.LocalDataSourceImpl
import com.example.weatherforecastapplication.data.models.Daos
import com.example.weatherforecastapplication.data.models.Favourites
import com.example.weatherforecastapplication.databinding.FragmentMapBinding
import com.example.weatherforecastapplication.favouritesFeature.viewModel.FavouritesViewModel
import com.example.weatherforecastapplication.utils.getAddressFromCoordinates
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

private const val TAG = "MapFragment"

class MapFragment : Fragment() , OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private lateinit var binding: FragmentMapBinding
    private lateinit var googleMap: GoogleMap
    private lateinit var favViewModel: FavouritesViewModel
    private lateinit var favViewModelFactory: FavouritesViewModel.Factory


    //location
    private lateinit var fusedClient: FusedLocationProviderClient
    private var latitude: Double? = 0.0
    private var longitude: Double? = 0.0
    private lateinit var name:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favViewModelFactory = FavouritesViewModel.Factory(
            LocalDataSourceImpl.getInstance(Daos(requireContext()))
        )
        favViewModel = ViewModelProvider(
            this, favViewModelFactory
        )[FavouritesViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       // val search = requireActivity().findViewById<View>(R.id.search)
       // search.visibility = View.GONE
        binding.map.onCreate(savedInstanceState)
        binding.map.onResume()
        binding.map.getMapAsync(this)
        binding.gps.setOnClickListener {
            getDeviceLocation()
        }

        getPlaces()
      binding.save.setOnClickListener {
          val favourites = Favourites(latitude!!, longitude!!, name)
          onSaveClicked(favourites)
      }
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation(){
        val locationCallBack = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                //if (isAdded) {
                val locationRequest = locationResult.lastLocation
                moveCamera(
                    LatLng(locationRequest!!.latitude, locationRequest.longitude),
                    15F,"My Location"
                )
                fusedClient.removeLocationUpdates(this)
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
    private fun moveCamera (latLng: LatLng, zoom:Float, tile: String)
    {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        val markerOptions = MarkerOptions()
            .position(latLng)
            .title(tile)
        googleMap.addMarker(markerOptions)
    }
    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap) {
            this.googleMap = map
        googleMap.setOnMapClickListener(this)
        getDeviceLocation()
        googleMap.isMyLocationEnabled = true; // put marker on your location
        googleMap.uiSettings.isMyLocationButtonEnabled = false;

    }
    private fun getPlaces(){
        if(!Places.isInitialized())
        {
            Places.initialize(
                requireContext(),
                "AIzaSyCAOMyuhbP1CAJ1H4WnnMSXyC_xhpu72tE",
            )
        }
        val autocompleteFragment =
            childFragmentManager.findFragmentById(
                R.id.autocomplete_fragment
            ) as AutocompleteSupportFragment
        autocompleteFragment
            .setPlaceFields(
                listOf(
                    Place.Field.ID,
                    Place.Field.NAME,
                    Place.Field.LAT_LNG,
                    Place.Field.VIEWPORT,
                    ))
        autocompleteFragment.setOnPlaceSelectedListener(
            object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                moveCamera(
                    LatLng(
                        place.viewport.center.latitude,
                        place.viewport.center.longitude
                    ),
                    15F,
                    place.name)
            }

            override fun onError(status: Status) {
                Log.i(TAG, "An error occurred: $status")
            }
        })
    }

    private fun onSaveClicked(favourites: Favourites){
        val sourceFragment =  arguments?.getString("source")
       navigateTo(sourceFragment?:"",favourites)
    }

    private fun navigateTo(sourceFragment: String,favourites: Favourites) {
        if(sourceFragment == "fav") {
            favViewModel
                .addWeatherToFavourites(favourites)
            val action=
                  MapFragmentDirections.actionMapFragmentToFavouritesFragment()
            val navController= Navigation.findNavController(requireView())
            navController.navigate(action)

        } else if(sourceFragment == "alert") {
            val action=
                  MapFragmentDirections.actionMapFragmentToAlertFragment(
                    latitude!!.toFloat(),
                    longitude!!.toFloat(),
                )
            val navController= Navigation.findNavController(requireView())
            navController.navigate(action)

        }else{
            val action=
                  MapFragmentDirections.actionMapFragmentToHomeFragment(
                    latitude!!.toFloat(), longitude!!.toFloat()
                )
            val navController= Navigation.findNavController(requireView())
            navController.navigate(action)
        }
    }

    override fun onMapClick(latLng: LatLng) {
        googleMap.clear()
        val markerOptions = MarkerOptions()
            .position(latLng)
            .title("Selected Place")
        googleMap.addMarker(markerOptions)
         latitude = latLng.latitude
         longitude = latLng.longitude
        name = getAddressFromCoordinates(requireContext(), latitude, longitude)
        binding.save.visibility = View.VISIBLE

    }
}