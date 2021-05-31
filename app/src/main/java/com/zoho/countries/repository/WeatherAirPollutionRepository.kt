package com.zoho.countries.repository

import com.zoho.countries.datasource.local.dao.WeatherAirPollutionDao
import com.zoho.countries.datasource.remote.WeatherRemoteDataSource
import com.zoho.countries.utils.performGetOperation
import javax.inject.Inject

class WeatherAirPollutionRepository @Inject constructor(
    private val weatherRemoteDataSource: WeatherRemoteDataSource,
    private val weatherAirPollutionDao: WeatherAirPollutionDao
) {

    fun getWeatherData(latitude: Double, longitude: Double) = performGetOperation(
        databaseQuery = { weatherAirPollutionDao.getWeatherData() },
        networkCall = { weatherRemoteDataSource.getWeatherData(latitude, longitude) },
        saveCallResult = { weatherAirPollutionDao.insertWeatherDetails(it) }
    )

    fun getAirPollutionData(latitude: Double, longitude: Double) = performGetOperation(
        databaseQuery = { weatherAirPollutionDao.getAirPollutionData(latitude) },
        networkCall = { weatherRemoteDataSource.getAirPollutionData(latitude, longitude) },
        saveCallResult = { weatherAirPollutionDao.insertAirPollutionDetails(it) }
    )

}