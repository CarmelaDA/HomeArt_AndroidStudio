package com.carmelart.homeart.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DataEntity(
    @ColumnInfo(name = "led") val led: Int,
    @PrimaryKey(autoGenerate = true) val id: Int = 0 // Primera columna
)