package com.zoho.countries.datasource.local.entities.converters

import com.google.gson.Gson
import androidx.room.TypeConverter

class ConverterDouble {

    @TypeConverter
    fun listToJson(value: List<Double>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): List<Double>? {
        val objects = Gson().fromJson(value, Array<Double>::class.java) as Array<Double>
        val list = objects.toList()
        return list
    }
}   