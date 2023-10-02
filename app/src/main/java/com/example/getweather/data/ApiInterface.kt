package com.example.getweather.data

import com.example.getweather.data.forecastModels.Forecast
import com.example.getweather.data.forecastModels.ForecastData
import com.example.getweather.data.models.CurrentWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    //get current weather using city name
    @GET("weather?")
    suspend fun getCurrentWeather(
        @Query("q") city : String,
        @Query("units") units : String,
        @Query("appid") apiKey : String,
    ):Response<CurrentWeather>

    //get hourly(3 hours) forecast using city name
    @GET("forecast?")
    suspend fun getForecast(
        @Query("q") city: String,
        @Query("units") units: String,
        @Query("appid") apiKey: String,
    ):Response<Forecast>
}