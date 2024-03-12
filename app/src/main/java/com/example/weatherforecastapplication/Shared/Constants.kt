package com.example.weatherforecastapplication.Shared

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.weatherforecastapplication.R

const val BASE_URL:String = "https://api.openweathermap.org/data/2.5/"
const val API_KEY:String = "93d6a7e396a4d256d304951fb4f21c3a"


@BindingAdapter("url")
fun loadImage(view: ImageView, url:String)
{
    // Picasso.get().load(url).error(R.drawable.ic_launcher_foreground).into(view)

    Glide.with(view.context).load("https://openweathermap.org/img/wn/$url.png")
        .apply(
            RequestOptions().override(200, 200)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_background)
        )
        .into(view)
}