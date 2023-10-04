package com.example.getweather.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.GridLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.getweather.data.adapter.hourlyWeatherAdapter
import com.example.getweather.data.forecastModels.ForecastData
import com.example.getweather.data.utils.RetrofitInstance
import com.example.getweather.data.utils.util
import com.example.getweather.data.viewmodels.MainActivityViewModel
import com.example.getweather.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel.getCurrentWeather()
        setContentView(binding.root)
        subscribeUi()

        //getCurrentWeather()
        //getForecast()
        //setupUi()
    }

    private fun subscribeUi(){

        //current weather
        viewModel.currentWeather.observe(this) { data ->
            //get IconId to generate URL
            val iconId = data.weather[0].icon

            //build image url
            val imageUrl = "${util.IMAGE_BASE_URL + iconId}@4x.png"

            //load the image in the view
            Picasso.get().load(imageUrl).into(binding.weatherImageView)

            //convert sunrise and sunset to minutes and hours
            binding.sunriseTimeTextView.text = SimpleDateFormat(
                "hh:mm a",
                Locale.ENGLISH
            ).format(Date(data.sys.sunrise.toLong() * 1000))

            binding.sunsetTimeTextView.text =
                SimpleDateFormat(
                    "hh:mm a",
                    Locale.ENGLISH
                ).format(data.sys.sunset * 1000)

            //for other view //data to be moved to view
            binding.apply {
                descriptionTextView.text = data.weather[0].description
                windSpeedTextView.text = "${data.wind.speed.toString()} KM/H | ${data.wind.deg.toInt()}°"
                locationTextView.text = "${data.name}"
                temperatureTextView.text = "${data.main.temp.toInt()}°"
                feelsLikeTemperatureTextView.text = " ${data.main.feels_like.toInt()}℃"
                minTenperatureTextView.text = "L: ${data.main.temp_min.toInt()}°"
                maxTemperatureTextView.text = "H: ${data.main.temp_max.toInt()}°"
                humidityTextView.text = "${data.main.humidity}%"
                pressureTextView.text = "${data.main.pressure}\nhPa"
                visibilityTextView.text = "${data.visibility} KM"
            }

        }


        //dailyForecast
        viewModel.dailyForecast.observe(this) { data ->

            //var forecastArray = arrayListOf<ForecastData>()

            //store data (Response body) as arrayList
           var forecastArray=data.list as ArrayList<ForecastData>

            //parse data into the recycler view
            val adapter = hourlyWeatherAdapter(forecastArray)


                binding.hourlyForecastRecyclerView.adapter = adapter
                 binding.hourlyForecastRecyclerView.layoutManager = GridLayoutManager(this, adapter.itemCount)
                //move to selected day forecast fragment activity when an item in recycler is clicked
                adapter.setOnItemClickListener(object : hourlyWeatherAdapter.onItemClickListener {
                    override fun onItemClick(position: Int) {
                        val intent =
                            Intent(applicationContext, selectedDayForecastActivity::class.java)
                        intent.putExtra("weather Date", adapter.getItemId(position))
                        startActivity(intent)
                    }
                })

        }
    }



    override fun onRestart() {
        subscribeUi()
        super.onRestart()
    }
}