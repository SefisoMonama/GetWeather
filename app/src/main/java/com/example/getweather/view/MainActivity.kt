package com.example.getweather.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.getweather.data.utils.RetrofitInstance
import com.example.getweather.data.utils.util
import com.example.getweather.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getCurrentWeather()
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
                    binding.temperatureTextView.text = response.body()!!.main.temp.toString()
                    //response.body().weather[2]
                }
            }else{
                binding.tempTextView.text = "No data found!"
            }
        }
    }

    //call get current weather when activity is open again
    override fun onRestart() {
        getCurrentWeather()
        super.onRestart()
    }
}