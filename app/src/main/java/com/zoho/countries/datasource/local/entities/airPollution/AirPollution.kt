package com.zoho.countries.datasource.local.entities.airPollution

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.zoho.countries.datasource.local.entities.converters.ConverterAirpollution

@Entity(tableName = "airpollution")
data class AirPollution(
    @Embedded
    val coord: Coord,
    @TypeConverters(ConverterAirpollution::class)
    val list: List<MainList>
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}