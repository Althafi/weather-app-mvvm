package com.example.weatherapp.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.adapter.ForecastWeatherAdapter
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.model.ForecastResponseApi
import com.example.weatherapp.model.WeatherResponseApi
import com.example.weatherapp.viewmodel.WeatherViewModel
import com.github.matteobattilana.weather.PrecipType
import eightbitlab.com.blurview.RenderScriptBlur
import retrofit2.Call
import retrofit2.Response
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: WeatherViewModel by viewModels()
    private val calender by lazy { Calendar.getInstance()}
    private val forecastWeatherAdapter by lazy { ForecastWeatherAdapter() }

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

            //current weather
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
                                val windSpeed = responseApi.wind?.speed ?: 0.0
                                val humidityPercent = responseApi.main?.humidity ?: 0
                                val currentTemp = responseApi.main?.temp ?: 0.0
                                val tempMin = responseApi.main?.tempMin ?: 0.0
                                val tempMax = responseApi.main?.tempMax ?: 0.0

                                tvStatusTemp.text = responseApi.weather?.get(0)?.main ?: "-"
                                tvWindSpeed.text = getString(R.string.wind_speed, windSpeed)
                                tvHumidityPercent.text = getString(R.string.humidity_percent, humidityPercent)
                                tvCurrentTemp.text = getString(R.string.current_temperature, currentTemp)
                                tvTempMin.text = getString(R.string.temp_min, tempMin)
                                tvTempMax.text = getString(R.string.temp_max, tempMax)


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
            //weather forecast
            var radius = 10f
            val decorView = window.decorView
            val rootView: ViewGroup? = (decorView.findViewById(android.R.id.content))
            val windowBackground = decorView.background

            rootView?.let {
                blurView.setupWith(it, RenderScriptBlur(this@MainActivity))
                    .setBlurRadius(radius)
                    .setFrameClearDrawable(windowBackground)
                blurView.outlineProvider = ViewOutlineProvider.BACKGROUND
                blurView.clipToOutline = true
            }

            //forecast weather
            viewModel.loadForecastWeather(lat, lon, "metric").enqueue(
                object : retrofit2.Callback<ForecastResponseApi> {
                    override fun onResponse(
                        call: Call<ForecastResponseApi>,
                        response: Response<ForecastResponseApi>
                    ) {
                        if (response.isSuccessful) {
                            val data = response.body()
                            blurView.visibility = View.VISIBLE

                            data?.let {
                                forecastWeatherAdapter.differ.submitList(it.list)
                                rvForecast.apply {
                                    layoutManager = LinearLayoutManager(
                                        this@MainActivity,
                                        LinearLayoutManager.HORIZONTAL,
                                        false
                                    )
                                    adapter = forecastWeatherAdapter
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<ForecastResponseApi>, t: Throwable) {
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
                initWeatherView()
                R.drawable.sunny_bg
            }
            "02","03","04" -> {
                initWeatherView()
                R.drawable.cloudy_bg
            }
            "09","10", "11" -> {
                initWeatherView()
                R.drawable.rainy_bg
            }
            "13" -> {
                initWeatherView()
                R.drawable.snow_bg
            }
            "50" -> {
                initWeatherView()
                R.drawable.haze_bg
            }
            else -> 0
        }
    }

    private fun setEffectRainSnow(icon: String) {
        when (icon.dropLast(1)) {
            "01" -> {
                initWeatherView()
            }
            "02","03","04" -> {
                initWeatherView()
            }
            "09","10", "11" -> {
                initWeatherView()
            }
            "13" -> {
                initWeatherView()
            }
            "50" -> {
                initWeatherView()
            }
        }
    }

    private fun initWeatherView() {
        binding.weatherView.apply {
            setWeatherData(PrecipType.CLEAR)
            angle = -20
            emissionRate = 100.0f
        }
    }
}

