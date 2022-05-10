package com.carmelart.homeart.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DataDAO {

    @Query("SELECT * FROM DataEntity WHERE id = 1 LIMIT 1")  // Me devuelve todos los datos de la tabla y luego la uso,  *=all
    fun getData(): DataEntity

    @Insert()
    fun addData(data: DataEntity)

    @Update()
    fun updateData(data: DataEntity) // primero select y luego update del dato que se quiera modificar

    /*@Query("DELETE FROM DataEntity WHERE name = :name")
    fun deleteData(name: String)*/

    /*@Query("INSERT INTO DataEntity (name, LED) VALUES (:name, :LED)") // ("miVector", 0, 0, 0, 0, 0)
    fun addData(name: String, LED: Int)*/
    //fun addData(name: DataEntity)

    //@Insert
    //fun addData(name: DataEntity) // buscar como usar

    // añadir query de update



}