package com.example.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.weatherapp.network.ApiClient
import com.example.weatherapp.network.ApiService
import com.example.weatherapp.repository.CityListRepository
import com.example.weatherapp.repository.WeatherRepository

class CityListViewModel(private val repository: CityListRepository): ViewModel() {

    constructor() : this(CityListRepository(ApiClient().getClient().create(ApiService::class.java)))

    fun getCityList(q: String, limit: Int) =
        repository.getCityList(q, limit)


}