package com.example.weatherforecastapplication.weatherFeature.view
import android.annotation.SuppressLint
import android.location.Geocoder
import android.opengl.Visibility
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.databinding.FragmentMapBinding
import com.example.weatherforecastapplication.favouritesFeature.model.LocalDataSourceImpl
import com.example.weatherforecastapplication.favouritesFeature.viewModel.FavouritesViewModel
import com.example.weatherforecastapplication.network.RemoteDataSourceImpl
import com.example.weatherforecastapplication.network.WeatherParam
import com.example.weatherforecastapplication.shared.API_KEY
import com.example.weatherforecastapplication.shared.getCurrentLang
import com.example.weatherforecastapplication.shared.getCurrentUnit
import com.example.weatherforecastapplication.weatherFeature.viewModel.WeatherViewModel
import com.example.weatherforecastapplication.weatherFeature.viewModel.WeatherViewModelFactory
import com.example.weatherforecastapplication.weatherRepository.WeatherRepositoryImpl
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
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Tile
import com.google.android.gms.maps.model.VisibleRegion
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import kotlinx.coroutines.launch
import java.lang.Exception
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener


private const val TAG = "MapFragment"

class MapFragment : Fragment() ,OnMapReadyCallback,GoogleMap.OnMapClickListener{

    private lateinit var binding: FragmentMapBinding
    private lateinit var googleMap: GoogleMap
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var weatherViewModelFactory: WeatherViewModelFactory
    private lateinit var favViewModel: FavouritesViewModel
    private lateinit var favViewModelFactory: FavouritesViewModel.Factory


    //location
    private lateinit var fusedClient: FusedLocationProviderClient
    private var latitude: Double? = 0.0
    private var longitude: Double? = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weatherViewModelFactory = WeatherViewModelFactory(
            WeatherRepositoryImpl.getInstance(RemoteDataSourceImpl,
                LocalDataSourceImpl.getInstance(requireContext()))
        )
        weatherViewModel = ViewModelProvider(this, weatherViewModelFactory)
            .get(WeatherViewModel::class.java)

        favViewModelFactory = FavouritesViewModel.Factory(
                LocalDataSourceImpl.getInstance(requireContext())
        )
        favViewModel = ViewModelProvider(this, favViewModelFactory)
            .get(FavouritesViewModel::class.java)
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


        val search = requireActivity().findViewById<View>(R.id.search)
        search.visibility = View.GONE
        binding.map.onCreate(savedInstanceState)
        binding.map.onResume()
        binding.map.getMapAsync(this)
        binding.gps.setOnClickListener {
            getDeviceLocation()
        }

        getPlaces()
      binding.save.setOnClickListener {
          onSaveClicked(latitude!!,longitude!!)
      }

        weatherViewModel.currentWeather.observe(requireActivity()) {
            favViewModel.addWeatherToFavourites(it)
            val action = MapFragmentDirections.actionMapFragmentToFavouritesFragment()
            Navigation.findNavController(requireView()).navigate(action)
        }



    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation(){
        val locationCallBack = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                //if (isAdded) {
                val locationRequest = locationResult.lastLocation
                moveCamera(
                    LatLng(locationRequest!!.latitude,locationRequest.longitude),
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


    private fun moveCamera (latLng: LatLng,zoom:Float,tile: String)
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
        googleMap.setMyLocationEnabled(true); // put marker on your location
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);

    }

    private fun getPlaces(){
        if(!Places.isInitialized())
        {
         Places.initialize(requireContext(),"AIzaSyCAOMyuhbP1CAJ1H4WnnMSXyC_xhpu72tE")
        }
        val autocompleteFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as  AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG,
            Place.Field.VIEWPORT))
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                moveCamera(LatLng(place.viewport.center.latitude,place.viewport.center.longitude),15F,place.name)
                latitude = place.viewport.center.latitude
                longitude = place.viewport.center.longitude
                binding.save.visibility = View.VISIBLE
                Log.i(TAG, "Place: ${place.name}, ${place.id}, ${place.latLng?.longitude}")
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: $status")
            }
        })
    }
    private fun geoLocate(text:String){
        try {

            val geocoder = Geocoder(requireActivity())
            val list = geocoder.getFromLocationName(text,10)
            if (!list.isNullOrEmpty()) {
                val listAdapter = mutableListOf<String>()
                for(item in list)
                {
                    listAdapter.add(item.getAddressLine(0))
                }
                moveCamera(LatLng(list.get(0).latitude,list.get(0).longitude),
                    15F,list.get(0).getAddressLine(0))
            }

                    Log.i(TAG, "geoLocate: $list")

        }catch (e:Exception)
        {
            Log.i(TAG, "geoLocate: $e")
        }
    }

    private fun onSaveClicked(latitude:Double,longitude:Double){
        val sourceFragment =  arguments?.getString("source")
        if(sourceFragment == "fav") {

            val weatherParam = WeatherParam(
                latitude.toDouble(), longitude.toDouble(), API_KEY,
                getCurrentUnit(requireActivity()),
                getCurrentLang(requireActivity()))
            weatherViewModel.getCurrentWeather(weatherParam)

        }else{
            val action= MapFragmentDirections.actionMapFragmentToHomeFragment(
                latitude.toFloat(),longitude.toFloat())
            val navController=  Navigation.findNavController(requireView())
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
        binding.save.visibility = View.VISIBLE

    }
}