package com.example.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.network.ApiClient
import com.example.weatherapp.network.ApiService

class WeatherViewModel(private val repository: WeatherRepository): ViewModel() {

    constructor() : this(WeatherRepository(ApiClient().getClient().create(ApiService::class.java)))

    fun loadCurrentWeather(lat: Double, lon: Double, units: String) =
        repository.getCurrentWeather(lat, lon, units)

    fun loadForecastWeather(lat: Double, lon: Double, units: String) =
        repository.getForecastWeather(lat, lon, units)

}