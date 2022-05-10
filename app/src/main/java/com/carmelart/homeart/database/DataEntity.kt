package com.carmelart.homeart.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DataEntity(
    //@ColumnInfo(name = "data") val data: String,
    //@ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "LEDazul") val LEDazul: Int,
    @ColumnInfo(name = "LEDrojo") val LEDrojo: Int,
    @PrimaryKey(autoGenerate = true) val id: Int = 0 // Primera columna
)