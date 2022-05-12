package com.carmelart.homeart.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DataEntity(
    @ColumnInfo(name = "luzSala") var luzSala: Int,
    @ColumnInfo(name = "luzComedor") var luzComedor: Int,
    @ColumnInfo(name = "segInt") var segInt: Int,
    @ColumnInfo(name = "segExt") var segExt: Int,
    @PrimaryKey(autoGenerate = true) val id: Int = 0 // First Column
)