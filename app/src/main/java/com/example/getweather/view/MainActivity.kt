package com.example.getweather.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.getweather.data.adapter.hourlyWeatherAdapter
import com.example.getweather.data.forecastModels.ForecastData
import com.example.getweather.data.utils.RetrofitInstance
import com.example.getweather.data.utils.util
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getCurrentWeather()
        getForecast()
        //setupUi()
    }

    private fun setupUi(){


    }

    //get current weather details
    private fun getCurrentWeather() {
        //Coroutine
        GlobalScope.launch(Dispatchers.IO) {
            val response = try {
                //get current weather of the specified queries
                RetrofitInstance.api.getCurrentWeather("Pretoria", "metric", util.API_KEY)

            }catch (e: IOException){
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(applicationContext, "app error ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                return@launch
            }catch (e: HttpException){
                Handler(Looper.getMainLooper()).post{
                    Toast.makeText(applicationContext, "http error ${e.message}", Toast.LENGTH_SHORT).show()
                }
                return@launch
            }

            //check response code and body for data extraction
            if (response.isSuccessful && response.body() != null){
                withContext(Dispatchers.IO){

                //whole response will be located in this variable in a JSON format
                    val data = response.body()!!

                //get IconId to generate URL
                    val iconId = data.weather[0].icon

                //build image url
                    val imageUrl = "${util.IMAGE_BASE_URL + iconId}@4x.png"


                    //Handler(Looper.getMainLooper()).post {

                        //}

                    runOnUiThread(){

                        //load the image in the view
                        Picasso.get().load(imageUrl).into(binding.weatherImageView)

                        //convert sunrise and sunset to minutes and hours
                        binding.sunriseTimeTextView.text= "Sunrise: " + SimpleDateFormat(
                            "hh:mm a",
                            Locale.ENGLISH
                        ).format(Date(data.sys.sunrise.toLong() * 1000))

                        binding.sunsetTimeTextView.text=
                            SimpleDateFormat(
                                "hh:mm a",
                                Locale.ENGLISH
                            ).format(data.sys.sunset * 1000)

                        //for other view //data to be moved to view
                        binding.apply {
                            descriptionTextView.text = data.weather[0].description
                            windSpeedTextView.text = "${data.wind.speed.toString()} KM/H | ${data.wind.deg.toInt()}°"
                            locationTextView.text = "${data.name}\n${data.sys.country}"
                            temperatureTextView.text= "${data.main.temp.toInt()}°"
                            feelsLikeTemperatureTextView.text = " ${data.main.feels_like.toInt()}℃"
                            minTenperatureTextView.text = "L: ${data.main.temp_min.toInt()}°"
                            maxTemperatureTextView.text = "H: ${data.main.temp_max.toInt()}°"
                            humidityTextView.text = "${data.main.humidity}%"
                            pressureTextView.text = "${data.main.pressure}\nhPa"
                            visibilityTextView.text = "${data.visibility} KM"
                        }
                    }

                }
            }
        }
    }

    //get hourly(3 hours interval) forecast for up to 5 days
    private fun getForecast(){
        //Coroutine
        GlobalScope.launch(Dispatchers.IO) {
            val response = try {
                //get current weather of the specified queries
                RetrofitInstance.api.getForecast("Pretoria", "metric", util.API_KEY)

            } catch (e: IOException) {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(applicationContext, "app error ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                return@launch
            } catch (e: HttpException) {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        applicationContext,
                        "http error ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return@launch
            }

            //check response code and body for data extraction
            if (response.isSuccessful && response.body() != null) {
                withContext(Dispatchers.Main) {

                    //store the response body in data variable
                    val data = response.body()!!

                    //
                    var forecastArray = arrayListOf<ForecastData>()

                    //store data (Response body) as arrayList
                    forecastArray=data.list as ArrayList<ForecastData>
                    //parse data into the recycler view
                    val adapter = hourlyWeatherAdapter(forecastArray)

                    runOnUiThread() {
                        binding.hourlyForecastRecyclerView.adapter = adapter
                        adapter.setOnItemClickListener(object : hourlyWeatherAdapter.onItemClickListener{
                            override fun onItemClick(position: Int) {
                                val intent = Intent(applicationContext, selectedDayForecastActivity::class.java)
                               intent.putExtra("weather Date", position)
                                startActivity(intent)
                            }

                        })
                    }
                }
            }
        }

    }

    //call get current weather when activity is open again
    override fun onRestart() {
        getCurrentWeather()
        super.onRestart()
    }
}