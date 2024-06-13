package com.example.weatherapp.repository

import com.example.weatherapp.model.WeatherResponseApi
import com.example.weatherapp.server.ApiService
import retrofit2.Call

class WeatherRepository(val api: ApiService) {

    fun getCurrentWeather(lat: Double, lon: Double, units: String)=
        api.getCurrentWeather(lat, lon, units, "3eb09a2103c39d1bc3b646b3619b3873")

}