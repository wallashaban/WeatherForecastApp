package com.example.weatherforecastapplication.weatherFeature.view

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.data.local.LocalDataSourceImpl
import com.example.weatherforecastapplication.data.models.Daos
import com.example.weatherforecastapplication.data.remote.RemoteDataSourceImpl
import com.example.weatherforecastapplication.data.repo.WeatherRepositoryImpl
import com.example.weatherforecastapplication.favouritesFeature.view.FavouritesFragmentDirections
import com.example.weatherforecastapplication.weatherFeature.viewModel.WeatherViewModel


class SplashFragment : Fragment() {


    private lateinit var weatherViewModel:WeatherViewModel
    private lateinit var weatherViewModelFactory:WeatherViewModel.Factory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weatherViewModelFactory = WeatherViewModel.Factory(
            WeatherRepositoryImpl.getInstance(
                RemoteDataSourceImpl,
                LocalDataSourceImpl.getInstance(Daos(requireContext()))
            )
        )
        weatherViewModel = ViewModelProvider(
            requireActivity(),
            weatherViewModelFactory
        )[WeatherViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("TAG", "SplashScreen: ")
        val sharedPreferences = requireContext()
            .getSharedPreferences("Splash", Context.MODE_PRIVATE)
        val isFirstTime = sharedPreferences.getBoolean("isFirstTime", true)

        if (weatherViewModel.isFirst) {
            Handler(Looper.getMainLooper()).postDelayed({
               Navigation. findNavController(requireView()).navigate(R.id.action_splashFragment_to_homeFragment)
            }, 2000)
            weatherViewModel.isFirst = false

        } else {
            Navigation.findNavController(requireView()).navigate(R.id.action_splashFragment_to_homeFragment)
        }
    }

}