package com.example.getweather.data.viewmodels

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.getweather.data.adapter.hourlyWeatherAdapter
import com.example.getweather.data.forecastModels.Forecast
import com.example.getweather.data.forecastModels.ForecastData
import com.example.getweather.data.models.CurrentWeather
import com.example.getweather.data.utils.RetrofitInstance
import com.example.getweather.data.utils.util
import com.example.getweather.view.selectedDayForecastActivity
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

annotation class ViewModelInject

class MainActivityViewModel : ViewModel() {

    //current weather
    private val _currentWeather: MutableLiveData<CurrentWeather> = MutableLiveData()
    val currentWeather: LiveData<CurrentWeather> get() = _currentWeather

    //forecast
    private val _dailyForecast: MutableLiveData<Forecast> = MutableLiveData()
    val dailyForecast : LiveData<Forecast> get() = _dailyForecast

    //
    private val _error: MutableLiveData<String> = MutableLiveData()
    val error: LiveData<String> get() = _error


    //get current weather details
    fun getCurrentWeather() {
        //Coroutine
        viewModelScope.launch {
            val response = try {
                //get current weather of the specified queries
                RetrofitInstance.api.getCurrentWeather("Pretoria", "metric", util.API_KEY)

            } catch (e: IOException) {
                _error.value = e.toString()
                return@launch
            } catch (e: HttpException) {
                _error.value = e.toString()
                return@launch
            }

            //check response code and body for data extraction
            if (response.isSuccessful && response.body() != null) {
                _currentWeather.value = response.body()!!
            } else {
                _error.value.toString()
            }
        }
    }


    private fun getForecast(){
        //Coroutine
        GlobalScope.launch(Dispatchers.IO) {
            val response = try {
                //get current weather of the specified queries
                RetrofitInstance.api.getForecast("Pretoria", "metric", util.API_KEY)

            } catch (e: IOException) {
                _error.value = e.toString()
                return@launch
            } catch (e: HttpException) {
                _error.value = e.toString()
                return@launch
            }

            //check response code and body for data extraction
            if (response.isSuccessful && response.body() != null) {
                _dailyForecast.value = response.body()!!
            } else {
                _error.value.toString()
            }

            }
        }

    }


