package com.example.weatherapp.repository

import com.example.weatherapp.network.ApiService

class CityListRepository(private val api: ApiService) {

    fun getCityList(q: String, limit: Int) =
        api.getCityList(q, limit, "3eb09a2103c39d1bc3b646b3619b3873")

}