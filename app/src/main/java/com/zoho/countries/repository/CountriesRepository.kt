package com.zoho.countries.repository

import com.zoho.countries.datasource.local.dao.CountryDao
import com.zoho.countries.datasource.remote.CountryRemoteDataSource
import com.zoho.countries.datasource.remote.WeatherRemoteDataSource
import com.zoho.countries.utils.performGetOperation
import javax.inject.Inject

class CountriesRepository @Inject constructor(
    private val countryRemoteDataSource: CountryRemoteDataSource,
    private val localDataSource: CountryDao
) {

    fun getCountries() = performGetOperation(
        databaseQuery = { localDataSource.getAllCountries() },
        networkCall = { countryRemoteDataSource.getCountries() },
        saveCallResult = { localDataSource.insertAll(it) }
    )

    fun getCountries(id: String) = localDataSource.getCountries(id)

    fun getSearchCountries(countryName: String) = localDataSource.getSearchCountries(countryName)
}