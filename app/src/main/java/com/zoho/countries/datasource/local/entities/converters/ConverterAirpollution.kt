package com.zoho.countries.datasource.local.entities.converters

import com.google.gson.Gson
import androidx.room.TypeConverter
import com.zoho.countries.datasource.local.entities.airPollution.MainList

class ConverterAirpollution {

    @TypeConverter
    fun listToJson(value: List<MainList>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): List<MainList>? {
        val objects = Gson().fromJson(value, Array<MainList>::class.java) as Array<MainList>
        val list = objects.toList()
        return list
    }
}   