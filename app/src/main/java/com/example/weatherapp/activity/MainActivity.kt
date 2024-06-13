package com.example.weatherapp.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.model.WeatherResponseApi
import com.example.weatherapp.viewmodel.WeatherViewModel
import com.github.matteobattilana.weather.PrecipType
import retrofit2.Call
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: WeatherViewModel by viewModels()
    private val calender by lazy { Calendar.getInstance()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT
        }

        binding.apply {
            var lat = 51.5087
            var lon = -0.1208
            var name = "London"
            tvCityName.text = name

            progressBar.visibility = View.VISIBLE

            viewModel.loadCurrentWeather(lat, lon, "metric").enqueue(
                object : retrofit2.Callback<WeatherResponseApi> {
                    override fun onResponse(
                        call: Call<WeatherResponseApi>,
                        response: retrofit2.Response<WeatherResponseApi>
                    ) {
                        if (response.isSuccessful) {
                            val weatherResponse = response.body()
                            progressBar.visibility = View.GONE
                            detailLayout.visibility = View.VISIBLE
                            weatherResponse?.let {responseApi ->
                                tvStatusTemp.text = responseApi.weather?.get(0)?.main ?: "-"
                                tvWindSpeed.text = responseApi.wind?.speed?.let {
                                    Math.round(it).toString()
                                } + "Km"
                                tvHumidityPercent.text = responseApi.main?.humidity.toString() + "%"
                                tvCurrentTemp.text = responseApi.main?.temp?.let {
                                    Math.round(it).toString()
                                } + "°"
                                tvTempMin.text = responseApi.main?.tempMin?.let {
                                    Math.round(it).toString()
                                } + "°"
                                tvTempMax.text = responseApi.main?.tempMax?.let {
                                    Math.round(it).toString()
                                } + "°"

                                val imageBackground = if (isNight()) R.drawable.night_bg else {
                                    setDynamicWallpaper(responseApi.weather?.get(0)?.icon ?: "-")
                                }
                                ivBg.setImageResource(imageBackground)
                                setEffectRainSnow(responseApi.weather?.get(0)?.icon ?: "-")
                            }
                        }
                    }
                    override fun onFailure(call: Call<WeatherResponseApi>, t: Throwable) {
                        Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }

    }

    //check if it is night
    private fun isNight() : Boolean {
        return calender.get(Calendar.HOUR_OF_DAY) >= 18
    }

    private fun setDynamicWallpaper(icon: String): Int {
        return when (icon.dropLast(1)) {
            "01" -> {
                initWeatherView(PrecipType.CLEAR)
                R.drawable.snow_bg
            }
            "02","03","04" -> {
                initWeatherView(PrecipType.CLEAR)
                R.drawable.cloudy_bg
            }
            "09","10", "11" -> {
                initWeatherView(PrecipType.CLEAR)
                R.drawable.rainy_bg
            }
            "13" -> {
                initWeatherView(PrecipType.CLEAR)
                R.drawable.snow_bg
            }
            "50" -> {
                initWeatherView(PrecipType.CLEAR)
                R.drawable.haze_bg
            }
            else -> 0
        }
    }

    private fun setEffectRainSnow(icon: String) {
        when (icon.dropLast(1)) {
            "01" -> {
                initWeatherView(PrecipType.CLEAR)
            }
            "02","03","04" -> {
                initWeatherView(PrecipType.CLEAR)
            }
            "09","10", "11" -> {
                initWeatherView(PrecipType.CLEAR)
            }
            "13" -> {
                initWeatherView(PrecipType.CLEAR)
            }
            "50" -> {
                initWeatherView(PrecipType.CLEAR)
            }
        }
    }

    private fun initWeatherView(type: PrecipType) {
        binding.weatherView.apply {
            setWeatherData(type)
            angle = -20
            emissionRate = 100.0f
        }
    }
}

