package com.example.weatherforecastapplication.settings.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.data.models.Language
import com.example.weatherforecastapplication.databinding.FragmentSettingsBinding
import com.example.weatherforecastapplication.settings.viewModel.SettingsViewModel
import com.example.weatherforecastapplication.data.models.Units
import com.example.weatherforecastapplication.data.models.WindSpeed
import com.example.weatherforecastapplication.utils.LocaleUtil
import com.example.weatherforecastapplication.utils.SpinnerUtils
import com.example.weatherforecastapplication.utils.Storage
import com.example.weatherforecastapplication.utils.checkConnectivity
import com.example.weatherforecastapplication.utils.setCardViewBackground
import com.example.weatherforecastapplication.utils.showSnackbar
import kotlinx.coroutines.launch

private const val TAG = "SettingsFragment"

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var lang: String
    private lateinit var settingsViewModel: SettingsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingsViewModel = ViewModelProvider(requireActivity())[SettingsViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.settingsCard.setCardBackgroundColor( setCardViewBackground(requireContext()))

        val tempItems = listOf(
            getString(R.string.celsius),
            getString(R.string.fahrenheit),
            getString(R.string.kelvin)
        )
        spinnerData(
            binding.tempSpinner,
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, tempItems)
        )
        val windItems = listOf(
            getString(R.string.meter_per_second),
            getString(R.string.mile_per_hour)
        )
        spinnerData(
            binding.windSpinner,
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, windItems)
        )

        val locationItems = listOf(
            getString(R.string.gps),
            getString(R.string.map)
        )
        spinnerData(
            binding.locationSpinner,
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, locationItems)
        )

        val langItems = listOf(
            getString(R.string.english_language),
            getString(R.string.arabic_language),
            getString(R.string.system_mode)
        )

        spinnerData(
            binding.langSpinner,
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, langItems)
        )

        val themeItems = listOf(
            getString(R.string.light_mode),
            getString(R.string.dark_mode),
            getString(R.string.system_mode)
        )
        spinnerData(
            binding.themeSpinner,
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, themeItems)
        )


        binding.windSpinner.setSelection(SpinnerUtils.getWindUnitSpinnerValue(requireContext()))
        binding.windSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                onWindUnitItemSelected(selectedItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.locationSpinner.setSelection(SpinnerUtils.getCurrentSpinnerLocationValue(requireContext()))
        binding.locationSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedItem = parent?.getItemAtPosition(position).toString()
                    onLocationItemSelected(selectedItem)
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        binding.themeSpinner.setSelection(Storage.getCurrentTheme(requireContext()) - 1)
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

        binding.tempSpinner.setSelection(SpinnerUtils.getTempSpinnerValue(requireContext()))
        binding.tempSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()

               onTempItemSelected(selectedItem)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.langSpinner.setSelection(SpinnerUtils.getLangSpinnerValue(requireContext()))
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

    private fun onTempItemSelected(selectedItem: String) {
        if(checkConnectivity(requireContext())) {
            when (selectedItem) {
                getString(R.string.celsius) -> {
                    if (Storage.getCurrentUnit(requireContext()) != "metric") {
                            settingsViewModel.changeUnit(Units.METRIC)
                        Storage.setUnit(requireContext(), "metric")
                    }
                }

                getString(R.string.fahrenheit) -> {
                    if (Storage.getCurrentUnit(requireContext()) != "standard") {
                            settingsViewModel.changeUnit(Units.STANDARD)

                        Storage.setUnit(
                            requireContext(),
                            "standard"
                        )
                    }

                }

                getString(R.string.kelvin) -> {
                    if (Storage.getCurrentUnit(requireContext()) != "imperial") {
                            settingsViewModel.changeUnit(Units.IMPERIAL)
                        Storage.setUnit(requireContext(), "imperial")
                    }
                }


            }
        }else{
            showSnackbar(requireActivity(),getString(R.string.noInternetMessage))
        }
    }
    private fun onLocationItemSelected(selectedItem: String) {
        if(checkConnectivity(requireContext())) {
            when (selectedItem) {
                getString(R.string.map) -> {
                    if (Storage.getCurrentLocation(requireContext()) != "map") {
                        Storage.setLocation(requireContext(), "map")
                        val action =
                            SettingsFragmentDirections.actionSettingsFragmentToMapFragment(
                                "map"
                            )
                        Navigation.findNavController(requireView()).navigate(action)

                    }
                }

                getString(R.string.gps) -> {
                    if (Storage.getCurrentLocation(requireContext()) != "gps") {
                        Storage.setLocation(requireContext(), "gps")
                        val action =
                            SettingsFragmentDirections.actionSettingsFragmentToHomeFragment(
                                1F,
                                0F
                            )
                        Navigation.findNavController(requireView()).navigate(action)
                    }
                }
            }
        }else{
            showSnackbar(requireActivity(),getString(R.string.noInternetMessage))
        }
    }
fun getSelectedItem():String{
    if(Storage.getPreferredLocale(requireContext()) == "ar")
        return getString(R.string.arabic_language)
    else if(Storage.getPreferredLocale(requireContext()) == "en")
        return getString(R.string.english_language)
    else
        return getString(R.string.system_mode)
}
    private fun onLangItemSelected(selectedItem: String) {
        Log.i(TAG, "onLangItemSelected:" +
                " $selectedItem ${Storage.getPreferredLocale(requireContext())}" +
                "   ${getSelectedItem()}")
        if (selectedItem!=getSelectedItem())
            when (selectedItem) {
                getString(R.string.english_language) -> {
                    lang = "en"
                    if (Storage.getPreferredLocale(requireContext()) != lang) {
                        Log.i(TAG, "onLangItemSelected: preferred_locale" +
                                "${Storage.getPreferredLocale(requireContext())}  " +
                                "$lang")
                        settingsViewModel.changeLanguage(Language.ENGLISH)
                        updateAppLocale(lang)
                    }
                }

                getString(R.string.system_mode) -> {
                    lang = "sys_def"
                    if (Storage.getPreferredLocale(requireContext()) != lang) {
                        Log.i(TAG, "onLangItemSelected: preferred_locale" +
                                "${Storage.getPreferredLocale(requireContext())}  " +
                                "$lang")
                        settingsViewModel.changeLanguage(Language.SYSTEM)
                        updateAppLocale(lang)
                    }
                }

                getString(R.string.arabic_language) -> {
                    lang = "ar"
                    if (Storage.getPreferredLocale(requireContext()) != lang) {
                        Log.i(TAG, "onLangItemSelected: preferred_locale" +
                                "${Storage.getPreferredLocale(requireContext())}  " +
                                "$lang")
                        settingsViewModel.changeLanguage(Language.ARABIC)
                        updateAppLocale(lang)
                    }
                }
            }

    }
    private fun spinnerData(spinner: Spinner, adapter: ArrayAdapter<String>) {
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter
    }
    private fun onThemeSelectedItem(parent: AdapterView<*>?, position: Int) {
        val selectedItem = parent?.getItemAtPosition(position).toString()
        if (position != Storage.getCurrentTheme(requireContext()) - 1) {
            when (selectedItem) {
                getString(R.string.light_mode) -> {
                    Storage.setTheme(requireContext(), 1)
                    settingsViewModel.changeMode(1)
                }

                getString(R.string.dark_mode) -> {
                    Storage.setTheme(requireContext(), 2)
                    settingsViewModel.changeMode(2)
                }

                getString(R.string.system_mode) -> {
                    Storage.setTheme(requireContext(), 3)
                    settingsViewModel.changeMode(3)
                }
            }
        }
    }
    private fun updateAppLocale(locale: String) {
        /*if (Storage.getPreferredLocale(requireContext()) == locale)
            return
        else {*/
       // Storage(requireContext()).setPreferredLocale(lang)
            Storage.setPreferredLocale(requireContext(), lang)
            LocaleUtil.applyLocalizedContext(requireContext(), locale)
            recreate(requireActivity())
        //}
    }
    private fun onWindUnitItemSelected(selectedItem: String) {
        when (selectedItem) {
            getString(R.string.meter_per_second) -> {
                if (Storage.getCurrentWindUnit(requireContext()) != "m/s") {
                        settingsViewModel.changeWindSpeed(WindSpeed.METER_PER_SEC)
                        Storage.setWindUnit(requireContext(), "m/s")
                }

            }
            getString(R.string.mile_per_hour) -> {
                if (Storage.getCurrentWindUnit(requireContext()) != "m/h") {
                        settingsViewModel.changeWindSpeed(WindSpeed.MILE_PER_HOUR)
                        Storage.setWindUnit(requireContext(), "m/h")
                }
            }
        }

    }
}
