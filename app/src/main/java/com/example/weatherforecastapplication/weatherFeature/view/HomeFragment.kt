package com.example.weatherforecastapplication.weatherFeature.view

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.SuperscriptSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.Shared.API_KEY
import com.example.weatherforecastapplication.databinding.FragmentHomeBinding
import com.example.weatherforecastapplication.databinding.HourlyWeatherLayoutBinding
import com.example.weatherforecastapplication.network.RemoteDataSourceImpl
import com.example.weatherforecastapplication.weatherFeature.viewModel.WeatherViewModel
import com.example.weatherforecastapplication.weatherFeature.viewModel.WeatherViewModelFactory
import com.example.weatherforecastapplication.weatherRepository.WeatherRepositoryImpl

class HomeFragment : Fragment() {
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var weatherViewModelFactory: WeatherViewModelFactory
    private lateinit var binding: FragmentHomeBinding
    private lateinit var manager:LinearLayoutManager
    private lateinit var hourlyWeatherAdapter: HourlyWeatherAdapter
    private lateinit var dailyanager:LinearLayoutManager
    private lateinit var dailyWeatherAdapter: DailyWeatherAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weatherViewModelFactory = WeatherViewModelFactory(
            WeatherRepositoryImpl.getInstance(RemoteDataSourceImpl)
        )
        weatherViewModel = ViewModelProvider(this,weatherViewModelFactory)
            .get(WeatherViewModel::class.java)
          weatherViewModel.getCurrentWeather(29.9792,31.1342,API_KEY)
        weatherViewModel.getFiveDaysForecast(29.9792,31.1342,API_KEY)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        manager = LinearLayoutManager(requireContext())
        manager .orientation = LinearLayoutManager.HORIZONTAL
        hourlyWeatherAdapter = HourlyWeatherAdapter(requireContext(),)
        binding.hourlyWeatherRv .layoutManager = manager
        binding.hourlyWeatherRv .adapter = hourlyWeatherAdapter

        dailyanager=LinearLayoutManager(requireContext())
        dailyanager.orientation = LinearLayoutManager.VERTICAL
        dailyWeatherAdapter = DailyWeatherAdapter(requireContext())
        binding.dailyWeatherRv.layoutManager = dailyanager
        binding.dailyWeatherRv.adapter = dailyWeatherAdapter


        weatherViewModel.fiveDaysForecast.observe(requireActivity()){
            hourlyWeatherAdapter.submitList(it.list)
            dailyWeatherAdapter.submitList(it.list)
        }


        weatherViewModel.currentWeather.observe(requireActivity()){
            Glide.with(view.context).load("https://openweathermap.org/img/wn/02d.png")
                .apply(
                    RequestOptions().override(200, 200)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_background)
                )
                .into(binding.imageView)
            binding.cityName.text = it.name
            val spannableString = SpannableString("${it.main.temp}\u00B0C")
            spannableString.setSpan(SuperscriptSpan(), it.main.temp.toString().length, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            binding.temp.text = spannableString//"${it.main.temp}°C"
            binding.weatherStatus.text = it.weather[0].main
        }

    }

}