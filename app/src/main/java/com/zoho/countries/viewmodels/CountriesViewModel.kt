package com.zoho.countries.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.zoho.countries.datasource.local.entities.country.Countries
import com.zoho.countries.repository.CountriesRepository
import com.zoho.countries.repository.WeatherAirPollutionRepository
import com.zoho.countries.utils.Resource

class  CountriesViewModel @ViewModelInject constructor(
    private val countriesRepository: CountriesRepository,
    private val weatherAirPollutionRepository: WeatherAirPollutionRepository
) :
    ViewModel() {

    val countryList: LiveData<Resource<List<Countries>>> = countriesRepository.getCountries()

    private val _searchItems = MutableLiveData<String>()

    private val _country = _searchItems.switchMap { countryName ->
        liveData {
            val repos = countriesRepository.getSearchCountries(countryName)
            emitSource(repos)
        }
    }

    val countries: LiveData<List<Countries>> = _country

    fun getWeatherData(latitude: Double, longitude: Double) =
        weatherAirPollutionRepository.getWeatherData(latitude, longitude)

    fun searchIt(countryName: String) {
        _searchItems.postValue(countryName)
    }
}