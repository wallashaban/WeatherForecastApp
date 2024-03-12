package com.example.weatherforecastapplication.weatherFeature.view

import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.model.CurrentWeather
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapplication.databinding.HourlyWeatherLayoutBinding

class HourlyWeatherAdapter(
    val context: Context,
   // var onProductClick: (CurrentWeather)->Unit,

    ) :ListAdapter<CurrentWeather,HourlyViewHolder>(ProductsDiffUtil()){
        lateinit var binding: HourlyWeatherLayoutBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        binding = DataBindingUtil
            .inflate(LayoutInflater.from(context),
                R.layout.hourly_weather_layout,parent,false)
        return HourlyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        val currentWeather:CurrentWeather = getItem(position)
        binding.weather = currentWeather
//        holder.constraintLayout.setOnClickListener {
//            onProductClick(product)
//        }
    }
}


class ProductsDiffUtil: DiffUtil.ItemCallback<CurrentWeather>(){
    override fun areItemsTheSame(oldItem: CurrentWeather, newItem: CurrentWeather): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CurrentWeather, newItem: CurrentWeather): Boolean {
        return oldItem==newItem
    }

}


class HourlyViewHolder(var layout: HourlyWeatherLayoutBinding) : RecyclerView.ViewHolder(layout.root)

