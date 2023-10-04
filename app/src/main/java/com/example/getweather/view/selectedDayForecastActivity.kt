package com.example.getweather.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.getweather.R
import com.example.getweather.databinding.ActivitySelectedDayForecastBinding

class selectedDayForecastActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySelectedDayForecastBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectedDayForecastBinding.inflate(layoutInflater)
        setupUi()
        setContentView(binding.root)
    }

    private fun setupUi() {
        val bundle : Bundle? = intent.extras
        val number = bundle!!.getString("weather Date")

        binding.SelectedDayForecastToolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.dateTextView.text = "selected weather details!"


    }
}