package com.carmelart.homeart.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DataEntity::class], version = 1)
abstract class DataDb : RoomDatabase() {
    abstract fun DataDAO(): DataDAO
}