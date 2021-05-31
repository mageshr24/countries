package com.zoho.countries.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.lifecycle.LiveData
import androidx.room.OnConflictStrategy
import com.zoho.countries.datasource.local.entities.airPollution.AirPollution
import com.zoho.countries.datasource.local.entities.weather.Weather

@Dao
interface WeatherAirPollutionDao {

    //Weather
    @Query("SELECT * FROM weather")
    fun getWeatherData(): LiveData<Weather>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherDetails(weather: Weather)

    //Airpollution
    @Query("SELECT * FROM airpollution  WHERE lat = :id")
    fun getAirPollutionData(id: Double): LiveData<AirPollution>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAirPollutionDetails(airPollution: AirPollution)
}