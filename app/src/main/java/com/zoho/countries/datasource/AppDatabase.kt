package com.zoho.countries.datasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zoho.countries.datasource.local.dao.CountryDao
import com.zoho.countries.datasource.local.dao.WeatherAirPollutionDao
import com.zoho.countries.datasource.local.entities.airPollution.AirPollution
import com.zoho.countries.datasource.local.entities.converters.*
import com.zoho.countries.datasource.local.entities.country.Countries
import com.zoho.countries.datasource.local.entities.weather.Weather

@Database(entities = arrayOf(Countries::class, Weather::class, AirPollution::class), version = 1, exportSchema = false)
@TypeConverters(ConverterLanguage::class, ConverterWeather::class, ConverterCurrency::class, ConverterDouble::class, ConverterAirpollution::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun countryDao(): CountryDao
    abstract fun weatherAirPollutionDao(): WeatherAirPollutionDao

    companion object {
        private var appDatabase: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            appDatabase ?: synchronized(this) {
                appDatabase ?: buildDatabase(context).also {
                    appDatabase = it
                }
            }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, AppDatabase::class.java, "countries")
                .fallbackToDestructiveMigration()
                .build()
    }
}