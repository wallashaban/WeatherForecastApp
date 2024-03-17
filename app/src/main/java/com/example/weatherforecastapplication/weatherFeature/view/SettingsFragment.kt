package com.example.weatherforecastapplication.weatherFeature.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.databinding.FragmentSettingsBinding
import com.example.weatherforecastapplication.shared.LocaleUtil
import com.example.weatherforecastapplication.shared.getCurrentLang
import com.example.weatherforecastapplication.shared.getCurrentLocation
import com.example.weatherforecastapplication.shared.getCurrentMode
import com.example.weatherforecastapplication.shared.getCurrentSpinnerLocationValue
import com.example.weatherforecastapplication.shared.getLangSpinnerValue
import com.example.weatherforecastapplication.shared.getTempSpinnerValue
import com.example.weatherforecastapplication.shared.saveSelectedLangToSharedPref
import com.example.weatherforecastapplication.shared.saveSelectedLocatioToSharedPref
import com.example.weatherforecastapplication.shared.saveSelectedModeToSharedPref
import com.example.weatherforecastapplication.shared.saveSelectedUnitToSharedPref
class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var lang:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        spinnerData(binding.tempSpinner,ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, tempItems))
        val windItems = listOf(
            getString(R.string.meter_per_second),
            getString(R.string.mile_per_hour)
        )
        spinnerData(binding.windSpinner,ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, windItems))

        val locationItems = listOf(
            getString(R.string.gps),
            getString(R.string.map)
        )
        spinnerData(binding.locationSpinner,ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, locationItems))

        val langItems = listOf(
            getString(R.string.english_language),
            getString(R.string.arabic_language),
           getString( R.string.system_mode)
        )
        spinnerData(binding.langSpinner,ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, langItems))

        val themeItems = listOf(
            getString(R.string.light_mode),
            getString(R.string.dark_mode),
            getString(R.string.system_mode)
        )
        spinnerData(binding.themeSpinner,ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, themeItems))
        binding.locationSpinner.setSelection(getCurrentSpinnerLocationValue(requireContext()))
        binding.locationSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                when (selectedItem) {
                    getString(R.string.map) -> {
                        if(getCurrentLocation(requireContext())!="map") {
                            saveSelectedLocatioToSharedPref(requireContext(), "map")
                            val action =
                                SettingsFragmentDirections.actionSettingsFragmentToMapFragment()
                            Navigation.findNavController(requireView()).navigate(action)
                        }

                    }
                    getString(R.string.gps) -> {
                        if(getCurrentLocation(requireContext())!="gps") {
                            saveSelectedLocatioToSharedPref(requireContext(), "gps")
                            val action =
                                SettingsFragmentDirections.actionSettingsFragmentToHomeFragment(
                                    0F,
                                    0F
                                )
                            Navigation.findNavController(requireView()).navigate(action)
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
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

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }

        binding.tempSpinner.setSelection(getTempSpinnerValue(requireContext()))
        binding.tempSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()

                when (selectedItem) {
                    getString(R.string.celsius) ->
                        saveSelectedUnitToSharedPref(requireContext(), "metric")
                    getString(R.string.fahrenheit) ->
                        saveSelectedUnitToSharedPref(
                            requireContext(),
                            "standard"
                        )
                    getString(R.string.kelvin) ->
                        saveSelectedUnitToSharedPref(requireContext(), "imperial")


                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }


        binding.langSpinner.setSelection(getLangSpinnerValue(requireContext()))
        binding.langSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                   onLangItemSelected(selectedItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun onLangItemSelected(selectedItem: String) {
        when (selectedItem) {
            getString(R.string.english_language) -> {
                lang = "en"
                if(getCurrentLang(requireContext())!=lang)
                    updateAppLocale(lang)
            }

            getString(R.string.system_mode) -> {
                lang = "sys_def"
                if(getCurrentLang(requireContext())!=lang)
                    updateAppLocale(lang)
            }
            getString(R.string.arabic_language) -> {
                lang = "ar"
                if(getCurrentLang(requireContext())!=lang)
                    updateAppLocale(lang)
            }
        }

    }

    private fun spinnerData(spinner: Spinner, adapter: ArrayAdapter<String>) {

        // adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter
    }

    private fun onThemeSelectedItem(parent: AdapterView<*>?, position: Int) {
        val selectedItem = parent?.getItemAtPosition(position).toString()
        when (selectedItem) {
            getString(R.string.light_mode) ->
                saveSelectedModeToSharedPref(requireContext(), 1)

            getString(R.string.dark_mode) -> {
                saveSelectedModeToSharedPref(requireContext(), 2)
            }

            getString(R.string.system_mode) -> {
                saveSelectedModeToSharedPref(requireContext(), 3)
            }
        }
    }

        private fun updateAppLocale(locale: String) {
            //storage.setPreferredLocale(locale)
            if (getCurrentLang(requireContext()) == locale)
                return
            else {
                saveSelectedLangToSharedPref(requireContext(), lang)
                LocaleUtil.applyLocalizedContext(requireContext(), locale)
                recreate(requireActivity())
            }
        }
    }
