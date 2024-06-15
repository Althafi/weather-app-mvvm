package com.example.weatherapp.repository

import com.example.weatherapp.network.ApiService

class WeatherRepository(private val api: ApiService) {

    fun getCurrentWeather(lat: Double, lon: Double, units: String)=
        api.getCurrentWeather(lat, lon, units, "3eb09a2103c39d1bc3b646b3619b3873")

    fun getForecastWeather(lat: Double, lon: Double, units: String)=
        api.getForecastWeather(lat, lon, units, "3eb09a2103c39d1bc3b646b3619b3873")

}