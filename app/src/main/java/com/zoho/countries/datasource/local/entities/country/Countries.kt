package com.zoho.countries.datasource.local.entities.country

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.zoho.countries.datasource.local.entities.converters.*

@Entity(tableName = "countries")
data class Countries(
    val alpha3Code: String?,
    val capital: String?,
    @TypeConverters(ConverterCurrency::class)
    val currencies: List<Currency>,
    val flag: String?,
    @TypeConverters(ConverterLanguage::class)
    val languages: List<Language>,
    @PrimaryKey
    val name: String,
    val region: String?,
    @TypeConverters(ConverterDouble::class)
    val latlng: List<Double>
)