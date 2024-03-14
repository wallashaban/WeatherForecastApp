package com.example.weatherforecastapplication.weatherFeature.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.weatherforecastapplication.Shared.getCurrentMode
import com.example.weatherforecastapplication.Shared.saveSelectedModeToSharedPref
import com.example.weatherforecastapplication.databinding.FragmentSettingsBinding



class SettingsFragment : Fragment() {
  private lateinit var binding:FragmentSettingsBinding
    var count = 0

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
        val themeItems = listOf("Light", "Dark","System")
        spinnerData(themeItems,binding.themeSpinner)

        binding.themeSpinner.setSelection(getCurrentMode(requireContext())-1)
        binding.themeSpinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                onThemeSelectedItem(parent,position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(
                    requireContext(),
                    "Nothing Selected",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

    }

    private fun spinnerData(items:List<String>,spinner: Spinner)
    {

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter
    }

    private fun onThemeSelectedItem(parent: AdapterView<*>?,position:Int)
    {
        count++
        val selectedItem = parent?.getItemAtPosition(position).toString()

        if(selectedItem == "Light"&& count>1) {
            Log.i("TAG", "onItemSelected: Light")
            saveSelectedModeToSharedPref(requireContext(),1)
        }else if(selectedItem == "Dark" && count>1){
            Log.i("TAG", "onItemSelected: Dark")
            saveSelectedModeToSharedPref(requireContext(),2) // remember it's been called twice
        }else if(selectedItem == "System" && count>1){
            Log.i("TAG", "onItemSelected: System")
            saveSelectedModeToSharedPref(requireContext(),3)
        }
    }
}
