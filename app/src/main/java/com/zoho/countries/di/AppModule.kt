package com.zoho.countries.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.zoho.countries.datasource.AppDatabase
import com.zoho.countries.datasource.local.dao.CountryDao
import com.zoho.countries.datasource.local.dao.WeatherAirPollutionDao
import com.zoho.countries.datasource.remote.CountryApiService
import com.zoho.countries.datasource.remote.CountryRemoteDataSource
import com.zoho.countries.datasource.remote.WeatherApiService
import com.zoho.countries.datasource.remote.WeatherRemoteDataSource
import com.zoho.countries.repository.CountriesRepository
import com.zoho.countries.repository.WeatherAirPollutionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    //CountryAPI
    @Singleton
    @Provides
    @Named("countriesapi")
    fun provideRetrofit(gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl("https://restcountries.eu/rest/v2/all/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideCountryApiService(@Named("countriesapi") retrofit: Retrofit): CountryApiService =
        retrofit.create(CountryApiService::class.java)

    @Singleton
    @Provides
    fun provideCountryRemoteDataSource(countryApiService: CountryApiService) =
        CountryRemoteDataSource(countryApiService)

    @Singleton
    @Provides
    fun provideCountryRepository(
        countryRemoteDataSource: CountryRemoteDataSource,
        localDataSource: CountryDao
    ) = CountriesRepository(countryRemoteDataSource, localDataSource)

    //WheatherAPI
    @Singleton
    @Provides
    @Named("weatherapi")
    fun provideRetrofit2(gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/data/2.5/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideWeatherApiService(@Named("weatherapi") retrofit: Retrofit): WeatherApiService =
        retrofit.create(WeatherApiService::class.java)

    @Singleton
    @Provides
    fun provideWheaterRemoteDataSource(weatherApiService: WeatherApiService) =
        WeatherRemoteDataSource(weatherApiService)

    @Singleton
    @Provides
    fun provideWeatherAirPollutionRepository(
        weatherRemoteDataSource: WeatherRemoteDataSource,
        weatherAirPollutionDao: WeatherAirPollutionDao
    ) = WeatherAirPollutionRepository(weatherRemoteDataSource, weatherAirPollutionDao)

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) =
        AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideCountryDao(appDatabase: AppDatabase) = appDatabase.countryDao()

    @Singleton
    @Provides
    fun provideWeatherAirPollutionDao(appDatabase: AppDatabase) =
        appDatabase.weatherAirPollutionDao()
}