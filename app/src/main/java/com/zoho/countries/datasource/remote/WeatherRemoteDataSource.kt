package com.zoho.countries.datasource.remote

import javax.inject.Inject

class WeatherRemoteDataSource @Inject constructor(
    private val apiService: WeatherApiService
) : BaseDataSource() {

    val APPID: String = "2e65127e909e178d0af311a81f39948c"

    suspend fun getWeatherData(latitude: Double, longitude: Double) = getResult {
        apiService.getWeatherData(
            latitude,
            longitude,
            "metric",
            APPID
        )
    }

    suspend fun getAirPollutionData(latitude: Double, longitude: Double) = getResult {
        apiService.getAirPollutionData(
            latitude,
            longitude,
            APPID
        )
    }
}