package com.zoho.countries.datasource.local.entities.country

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency")
data class Currency(
    val code: String?,
    val name: String?,
    val symbol: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}