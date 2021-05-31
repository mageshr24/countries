package com.zoho.countries.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.lifecycle.LiveData
import androidx.room.OnConflictStrategy
import com.zoho.countries.datasource.local.entities.country.Countries

@Dao
interface CountryDao {

    //Countries
    @Query("SELECT * FROM countries")
    fun getAllCountries(): LiveData<List<Countries>>

    @Query("SELECT * FROM countries WHERE name = :id")
    fun getCountries(id: String): LiveData<Countries>

    @Query("SELECT * FROM countries  WHERE name  LIKE '%' || :countryName || '%'")
    fun getSearchCountries(countryName: String): LiveData<List<Countries>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(countries: List<Countries>)
}