package com.example.weatherforecastapplication.weatherFeature.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.weatherforecastapplication.databinding.FragmentSettingsBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SettingsFragment : Fragment() {
  private lateinit var binding:FragmentSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tempItems = listOf("C", "F", "K")
        spinnerData(tempItems,binding.tempSpinner)
        val windItems = listOf("Meter/Sec", "Mile/Hour")
        spinnerData(windItems,binding.windSpinner)
        val locationItems = listOf("GPS", "Map")
        spinnerData(locationItems,binding.locationSpinner)
        val langItems = listOf("English", "arabic")
        spinnerData(langItems,binding.langSpinner)
        val themeItems = listOf("Light", "Dark")
        spinnerData(themeItems,binding.themeSpinner)
    }

    private fun spinnerData(items:List<String>,spinner: Spinner)
    {

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter
    }
}
