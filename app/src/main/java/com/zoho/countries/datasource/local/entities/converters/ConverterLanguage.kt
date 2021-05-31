package com.zoho.countries.datasource.local.entities.converters

import com.google.gson.Gson
import androidx.room.TypeConverter
import com.zoho.countries.datasource.local.entities.country.Language

class ConverterLanguage {

    @TypeConverter
    fun listToJson(value: List<Language>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): List<Language>? {
        val objects = Gson().fromJson(value, Array<Language>::class.java) as Array<Language>
        val list = objects.toList()
        return list
    }
}