package com.carmelart.homeart.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DataDAO {

    @Query("SELECT * FROM DataEntity WHERE name = :name") // Me devuelve todos los datos de la tabla y luego la uso
    fun getData(name: String): DataEntity

    @Query("DELETE FROM DataEntity WHERE name = :name")
    fun deleteData(name: String)

    /*@Query("INSERT INTO DataEntity (name, LED) VALUES (:name, :LED)") // ("miVector", 0, 0, 0, 0, 0)
    fun addData(name: String, LED: Int)*/
    //fun addData(name: DataEntity)

    //@Insert
    //fun addData(name: DataEntity) // buscar como usar

    // añadir query de update



}