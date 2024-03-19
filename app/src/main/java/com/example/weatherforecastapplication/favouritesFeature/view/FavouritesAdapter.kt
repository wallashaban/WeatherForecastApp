package com.example.weatherforecastapplication.favouritesFeature.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapplication.R
import com.example.weatherforecastapplication.databinding.FavLayoutBinding
import com.example.weatherforecastapplication.model.CurrentWeather

class FavouritesAdapter(
    val context: Context,
    private val listener: (weather:CurrentWeather)->Unit,
    private val favListener: (latitude:Double, longitude:Double)->Unit
) : ListAdapter<CurrentWeather, FavouritesViewHolder>(
    FavouritesDiffUtil()
) {

    private lateinit var binding: FavLayoutBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesViewHolder {
        binding = DataBindingUtil
            .inflate(
                LayoutInflater.from(context),
                R.layout.fav_layout, parent, false
            )
        return FavouritesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavouritesViewHolder, position: Int) {
        val current = getItem(position)
        binding.weather = current
        binding.delete.setOnClickListener {
            listener.invoke(current)
        }
        binding.favConstraint.setOnClickListener {
            favListener.invoke(
                current.coord.latitude,
                current.coord.longitude
            )
        }
    }
}

class FavouritesViewHolder(val layout: FavLayoutBinding) : RecyclerView.ViewHolder(layout.root)
class FavouritesDiffUtil : DiffUtil.ItemCallback<CurrentWeather>() {
    override fun areItemsTheSame(oldItem: CurrentWeather, newItem: CurrentWeather): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CurrentWeather, newItem: CurrentWeather): Boolean {
        return oldItem == newItem
    }

}