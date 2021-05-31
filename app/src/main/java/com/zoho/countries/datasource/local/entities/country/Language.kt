package com.zoho.countries.datasource.local.entities.country

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "language")
data class Language(
    val name: String?,
    val nativeName: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}