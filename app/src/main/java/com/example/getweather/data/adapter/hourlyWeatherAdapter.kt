package com.example.getweather.data.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.getweather.data.forecastModels.ForecastData
import com.example.getweather.data.utils.util
import com.example.getweather.databinding.RecyclerViewLayoutBinding
import com.squareup.picasso.Picasso
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class hourlyWeatherAdapter(private val forecastArray: ArrayList<ForecastData>) : RecyclerView.Adapter<hourlyWeatherAdapter.ViewHolder>() {

    //for item on click listener on our recycler view
    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }


    class ViewHolder(val binding: RecyclerViewLayoutBinding, listener: onItemClickListener) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.recyclerViewLinearLayout.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RecyclerViewLayoutBinding.inflate(LayoutInflater.from(parent.context),parent, false), mListener)
    }

    override fun getItemCount(): Int {
        return forecastArray.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = forecastArray[position]
        holder.binding.apply {
            val imageIcon = currentItem.weather[0].icon
            val imageUrl = util.IMAGE_BASE_URL+imageIcon+"@4x.png"

            Picasso.get().load(imageUrl).into(dailyForecastImageImageView)
            dailyForecastTemperatureTextView.text = "${currentItem.main.temp.toInt()}℃"
            dailyForecasttimeTextView.text = displayTime(currentItem.dt_txt)
            forecastDateTextView.text = displayDate(currentItem.dt_txt)
            //forecastDayTextView.text= displayDay("${currentItem.dt_txt} - ")

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayTime(dtTxt: String): CharSequence? {
        val input = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val output = DateTimeFormatter.ofPattern("HH:mm")
        val dateTime = LocalDateTime.parse(dtTxt,input)
        return output.format(dateTime)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayDate(dtTxt: String): CharSequence? {
        val input = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val output = DateTimeFormatter.ofPattern("E - dd MMM")
        val dateTime = LocalDateTime.parse(dtTxt, input)
        return output.format(dateTime)
    }
}