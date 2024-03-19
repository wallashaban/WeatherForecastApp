package com.example.weatherforecastapplication.alertFeature.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.databinding.FragmentAlertBinding
import com.example.weatherforecastapplication.databinding.FragmentFavouritesBinding
import com.example.weatherforecastapplication.favouritesFeature.view.FavouritesFragmentDirections


class AlertFragment : Fragment() {

    private lateinit var binding: FragmentAlertBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentAlertBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.alertAdd.setOnClickListener {
            val action= AlertFragmentDirections
                .actionAlertFragmentToMapFragment("alert")
            Navigation.findNavController(requireView())
                .navigate(action)
        }
    }

}