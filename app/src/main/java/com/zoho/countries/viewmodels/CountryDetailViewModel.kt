package com.zoho.countries.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.MutableLiveData
import androidx.hilt.lifecycle.ViewModelInject
import com.zoho.countries.datasource.local.entities.country.Countries
import com.zoho.countries.repository.CountriesRepository
import com.zoho.countries.repository.WeatherAirPollutionRepository

class CountryDetailViewModel @ViewModelInject constructor(
    private val countriesRepository: CountriesRepository,
    private val weatherAirPollutionRepository: WeatherAirPollutionRepository
) : ViewModel() {

    private val _id = MutableLiveData<String>()

    private val _country = _id.switchMap { id ->
        countriesRepository.getCountries(id)
    }

    val countries: LiveData<Countries> = _country

    fun start(id: String) {
        _id.value = id
    }

    fun getAirPollution(latitude: Double, longitude: Double) =
        weatherAirPollutionRepository.getAirPollutionData(latitude, longitude)
}
