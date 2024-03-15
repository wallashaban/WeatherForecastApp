package com.example.weatherforecastapplication.weatherFeature.view

import android.content.Context
import android.content.res.Resources
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
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.databinding.FragmentSettingsBinding
import com.example.weatherforecastapplication.shared.LocaleHelper
import com.example.weatherforecastapplication.shared.getCurrentMode
import com.example.weatherforecastapplication.shared.getCurrentUnit
import com.example.weatherforecastapplication.shared.getTempSpinnerValue
import com.example.weatherforecastapplication.shared.saveSelectedModeToSharedPref
import com.example.weatherforecastapplication.shared.saveSelectedUnitToSharedPref


class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    var count = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tempItems = listOf(
            getString(R.string.celsius),
            getString(R.string.fahrenheit),
            getString(R.string.kelvin)
        )

        spinnerData(tempItems, binding.tempSpinner)
        val windItems = listOf(
            getString(R.string.meter_per_second),
            getString(R.string.mile_per_hour)
        )
        spinnerData(windItems, binding.windSpinner)

        val locationItems = listOf(
            getString(R.string.gps),
            getString(R.string.map)
        )
        spinnerData(locationItems, binding.locationSpinner)

        val langItems = listOf(
            getString(R.string.english_language),
            getString(R.string.arabic_language)
        )
        spinnerData(langItems, binding.langSpinner)

        val themeItems = listOf(
            getString(R.string.light_mode),
            getString(R.string.dark_mode),
            getString(R.string.system_mode)
        )
        spinnerData(themeItems, binding.themeSpinner)

        binding.themeSpinner.setSelection(getCurrentMode(requireContext()) - 1)
        binding.themeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                onThemeSelectedItem(parent, position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(
                    requireContext(),
                    "Nothing Selected",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }


        binding.tempSpinner.setSelection(getTempSpinnerValue(requireContext()))
        binding.tempSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                count++
                    val selectedItem = parent?.getItemAtPosition(position).toString()

                    if (selectedItem == getString(R.string.celsius) && count > 1) {
                        Log.i("TAG", "onItemSelected: Light")
                        saveSelectedUnitToSharedPref(requireContext(), "metric")
                    } else if (selectedItem == getString(R.string.fahrenheit) && count > 1) {
                        Log.i("TAG", "onItemSelected: Dark")
                        saveSelectedUnitToSharedPref(requireContext(), "standard") // remember it's been called twice
                    } else if (selectedItem == getString(R.string.kelvin) && count > 1) {
                        Log.i("TAG", "onItemSelected: System")
                        saveSelectedUnitToSharedPref(requireContext(), "imperial")
                    }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(
                    requireContext(),
                    "Nothing Selected",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }







        binding.langSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                count++
                if (count > 1) {
                    Log.i("TAG", "onItemSelected: Lang")
                    val context: Context =
                        LocaleHelper.setLocale(requireActivity(), "ar")
                    var resources: Resources = context.resources
                }
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

    private fun spinnerData(items: List<String>, spinner: Spinner) {

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter
    }

    private fun onThemeSelectedItem(parent: AdapterView<*>?, position: Int) {
        count++
        val selectedItem = parent?.getItemAtPosition(position).toString()

        if (selectedItem == getString(R.string.light_mode) && count > 1) {
            Log.i("TAG", "onItemSelected: Light")
            saveSelectedModeToSharedPref(requireContext(), 1)
        } else if (selectedItem == getString(R.string.dark_mode) && count > 1) {
            Log.i("TAG", "onItemSelected: Dark")
            saveSelectedModeToSharedPref(requireContext(), 2) // remember it's been called twice
        } else if (selectedItem == getString(R.string.system_mode) && count > 1) {
            Log.i("TAG", "onItemSelected: System")
            saveSelectedModeToSharedPref(requireContext(), 3)
        }
    }
}
