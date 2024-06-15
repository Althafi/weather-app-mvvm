package com.example.weatherapp.network

import com.example.weatherapp.model.CityListResponseApi
import com.example.weatherapp.model.ForecastResponseApi
import com.example.weatherapp.model.WeatherResponseApi
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("data/2.5/weather")
    fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("appid") apiKey: String
    ): Call<WeatherResponseApi>

    @GET("data/2.5/forecast")
    fun getForecastWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("appid") apiKey: String
    ): Call<ForecastResponseApi>

    @GET("geo/1.0/direct")
    fun getCityList(
        @Query("q") cityName: String,
        @Query("limit") limit: Int,
        @Query("appid") apiKey: String
    ): Call<CityListResponseApi>



}