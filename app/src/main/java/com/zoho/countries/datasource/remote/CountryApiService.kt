package com.zoho.countries.datasource.remote

import com.zoho.countries.datasource.local.entities.country.CountryList
import retrofit2.http.GET
import retrofit2.Response

interface CountryApiService {

    @GET(".")
    suspend fun getAllCountries(): Response<CountryList>
}