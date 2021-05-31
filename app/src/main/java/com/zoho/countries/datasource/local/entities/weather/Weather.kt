package com.zoho.countries.datasource.local.entities.weather

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.zoho.countries.datasource.local.entities.converters.ConverterWeather

@Entity(tableName = "weather")
data class Weather(
    val name: String?,
    val id: Int,
    @Embedded
    val main: Main,
    @TypeConverters(ConverterWeather::class)
    val weather: List<WeatherX>
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}