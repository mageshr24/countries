package com.zoho.countries.datasource.local.entities.converters

import com.google.gson.Gson
import androidx.room.TypeConverter
import com.zoho.countries.datasource.local.entities.weather.WeatherX

class ConverterWeather {

    @TypeConverter
    fun listToJson(value: List<WeatherX>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): List<WeatherX>? {
        val objects = Gson().fromJson(value, Array<WeatherX>::class.java) as Array<WeatherX>
        val list = objects.toList()
        return list
    }
}   