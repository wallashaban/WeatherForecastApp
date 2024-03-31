package com.example.weatherforecastapplication.favouritesFeature.view

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.databinding.FragmentFavouritesBinding
import com.example.weatherforecastapplication.data.models.Favourites
import com.example.weatherforecastapplication.data.local.LocalDataSourceImpl
import com.example.weatherforecastapplication.data.models.Daos
import com.example.weatherforecastapplication.favouritesFeature.viewModel.FavouritesViewModel
import com.example.weatherforecastapplication.utils.ApiState
import com.example.weatherforecastapplication.utils.checkConnectivity
import com.example.weatherforecastapplication.utils.popMapFragmentFromTheBackStack
import com.example.weatherforecastapplication.utils.showSnackbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch


class FavouritesFragment : Fragment() {

    private lateinit var binding: FragmentFavouritesBinding
    private lateinit var favViewModel: FavouritesViewModel
    private lateinit var favViewModelFactory: FavouritesViewModel.Factory
    private lateinit var manager: LinearLayoutManager
    private lateinit var adapter: FavouritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favViewModelFactory = FavouritesViewModel.Factory(
            LocalDataSourceImpl.getInstance(Daos(requireContext()))
        )
        favViewModel = ViewModelProvider(
            this, favViewModelFactory
        )[FavouritesViewModel::class.java]
        favViewModel.getFavWeather()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouritesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        popMapFragmentFromTheBackStack(requireView())

        val onDeleteClickListener = { weather: Favourites ->
            showFavDialog(weather)
        }
        val onFavClickListener = { latitude: Double, longitude: Double ->
            if (checkConnectivity(requireContext())) {
                val action = FavouritesFragmentDirections
                    .actionFavouritesFragmentToHomeFragment(
                        latitude.toFloat(), longitude.toFloat()
                    )
                Navigation.findNavController(requireView()).navigate(action)
            } else {
                showSnackbar(requireActivity(), getString(R.string.noInternetMessage))
            }
        }

        adapter = FavouritesAdapter(
            requireContext(),
            onDeleteClickListener, onFavClickListener
        )

        manager = LinearLayoutManager(requireContext())
        manager.orientation = LinearLayoutManager.VERTICAL
        binding.favouritesRV.layoutManager = manager
        binding.favouritesRV.adapter = adapter

        binding.floatingActionButton.setOnClickListener {
            if (checkConnectivity(requireContext())) {
                val action = FavouritesFragmentDirections
                    .actionFavouritesFragmentToMapFragment(getString(R.string.fav))
                Navigation.findNavController(requireView())
                    .navigate(action)
            } else {
                showSnackbar(requireActivity(), getString(R.string.noInternetMessage))
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                favViewModel.favourites.collect { result ->
                    when (result) {
                        is ApiState.Success -> {
                            if (result.data.isEmpty()) {
                                binding.noFav.visibility = View.VISIBLE
                            } else {
                                binding.noFav.visibility = View.GONE
                            }
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

    private fun showFavDialog(weather: Favourites) {
        MaterialAlertDialogBuilder(requireContext())
            //.setTitle(R.string.dialogTitle)
            .setMessage(getString(R.string.alertMessage))
            .setPositiveButton(
                getString(R.string.yes)
            ) { dialog: DialogInterface?, which: Int ->
                favViewModel.deleteWeatherFromFavourites(weather)
            }
            .setNegativeButton(
                getString(R.string.no)
            ) { dialog: DialogInterface?, which: Int -> }
            .show()
    }
}


