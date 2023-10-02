package com.example.getweather.data.adapter

import android.os.Build
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.getweather.data.forecastModels.ForecastData
import com.example.getweather.data.utils.util
import com.example.getweather.databinding.RecyclerViewLayoutBinding
import com.squareup.picasso.Picasso
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class hourlyWeatherAdapter(private val forecastArray: ArrayList<ForecastData>) : RecyclerView.Adapter<hourlyWeatherAdapter.ViewHolder>() {
    class ViewHolder(val binding: RecyclerViewLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return forecastArray.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = forecastArray[position]
        holder.binding.apply {
            val imageIcon = currentItem.weather[0].icon
            val imageUrl = util.IMAGE_BASE_URL+imageIcon+".png"

            Picasso.get().load(imageUrl).into(dailyForecastImageImageView)
            dailyForecastTemperatureTextView.text = "${currentItem.main.temp.toInt()}℃"

            dailyForecasttimeTextView.text = displayTime(currentItem.dt_txt)

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayTime(dtTxt: String): CharSequence? {
        val input = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val output = DateTimeFormatter.ofPattern("HH")
        val dateTime = LocalDateTime.parse(dtTxt,input)
        return output.format(dateTime)

    }


}