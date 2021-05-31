package com.zoho.countries.datasource.remote

import javax.inject.Inject

class CountryRemoteDataSource @Inject constructor(
    private val apiService: CountryApiService
) : BaseDataSource() {

    suspend fun getCountries() = getResult { apiService.getAllCountries() }

}