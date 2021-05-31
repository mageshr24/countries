package com.zoho.countries.datasource.remote

import com.zoho.countries.datasource.local.entities.airPollution.AirPollution
import com.zoho.countries.datasource.local.entities.weather.Weather
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Query

interface WeatherApiService {

    @GET("weather")
    suspend fun getWeatherData(
        @Query(value = "lat", encoded = true) lat: Double?,
        @Query(value = "lon", encoded = true) lon: Double?,
        @Query(value = "units", encoded = true) units: String?,
        @Query(value = "APPID", encoded = true) APPID: String?
    ): Response<Weather>

    @GET("air_pollution")
    suspend fun getAirPollutionData(
        @Query(value = "lat", encoded = true) lat: Double?,
        @Query(value = "lon", encoded = true) lon: Double?,
        @Query(value = "APPID", encoded = true) APPID: String?
    ): Response<AirPollution>
}