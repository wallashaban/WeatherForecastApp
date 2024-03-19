package com.example.weatherforecastapplication.favouritesFeature.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.databinding.FragmentFavouritesBinding
import com.example.weatherforecastapplication.favouritesFeature.model.LocalDataSourceImpl
import com.example.weatherforecastapplication.favouritesFeature.viewModel.FavouritesViewModel
import com.example.weatherforecastapplication.model.CurrentWeather
import com.example.weatherforecastapplication.shared.ApiState
import kotlinx.coroutines.launch


class FavouritesFragment : Fragment(){

    private lateinit var binding: FragmentFavouritesBinding
    private lateinit var favViewModel: FavouritesViewModel
    private lateinit var favViewModelFactory: FavouritesViewModel.Factory
    private lateinit var manager: LinearLayoutManager
    private lateinit var adapter: FavouritesAdapter
    private var lat = 0F
    private var long = 0F
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favViewModelFactory = FavouritesViewModel.Factory(
            LocalDataSourceImpl.getInstance(requireContext())
        )
        favViewModel = ViewModelProvider(this, favViewModelFactory)
            .get(FavouritesViewModel::class.java)
        favViewModel.getFavWeather()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouritesBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // pop map fragment from the back stack
        val navController=  Navigation.findNavController(requireView())
        navController.popBackStack(R.id.mapFragment,true)


        val onDeleteClickListener ={weather: CurrentWeather->
            favViewModel.deleteWeatherFromFavourites(weather)
        }
        val onFavClickListener = {latitude:Double,longitude:Double->
            val action= FavouritesFragmentDirections
                .actionFavouritesFragmentToHomeFragment(
                    latitude.toFloat(),longitude.toFloat())
            Navigation.findNavController(requireView()).navigate(action)
        }

        adapter = FavouritesAdapter(requireContext(),
            onDeleteClickListener , onFavClickListener)

        manager = LinearLayoutManager(requireContext())
        manager.orientation = LinearLayoutManager.VERTICAL
        binding.favouritesRV.layoutManager = manager
        binding.favouritesRV.adapter = adapter

        binding.floatingActionButton.setOnClickListener{
            val action= FavouritesFragmentDirections
                .actionFavouritesFragmentToMapFragment("fav")
            Navigation.findNavController(requireView())
                .navigate(action)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                favViewModel.favourites.collect { result ->
                    when (result) {
                        is ApiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            adapter.submitList(result.data)
                        }
                        is ApiState.Failure -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                requireContext(),
                                result.error.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        is ApiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }



}