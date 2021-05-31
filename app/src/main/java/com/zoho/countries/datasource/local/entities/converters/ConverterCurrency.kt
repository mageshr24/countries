package com.zoho.countries.datasource.local.entities.converters

import com.google.gson.Gson
import androidx.room.TypeConverter
import com.zoho.countries.datasource.local.entities.country.Currency

class ConverterCurrency {

    @TypeConverter
    fun listToJson(value: List<Currency>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): List<Currency>? {
        val objects = Gson().fromJson(value, Array<Currency>::class.java) as Array<Currency>
        val list = objects.toList()
        return list
    }
}   