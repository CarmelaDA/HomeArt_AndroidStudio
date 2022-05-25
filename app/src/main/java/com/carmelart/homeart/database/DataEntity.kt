package com.carmelart.homeart.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DataEntity(

    // ILUMINACIÓN
    @ColumnInfo(name = "luzSala") var luzSala: Int,
    @ColumnInfo(name = "luzComedor") var luzComedor: Int,
    @ColumnInfo(name = "luzAmbiente") var luzAmbiente: Int,
    @ColumnInfo(name = "luzRecibidor") var luzRecibidor: Int,
    @ColumnInfo(name = "luzCocina") var luzCocina: Int,
    @ColumnInfo(name = "luzFregadero") var luzFregadero: Int,
    @ColumnInfo(name = "luzBano") var luzBano: Int,
    @ColumnInfo(name = "luzEspejo") var luzEspejo: Int,
    @ColumnInfo(name = "luzDormitorio") var luzDormitorio: Int,
    @ColumnInfo(name = "luzMesitaIzq") var luzMesitaIzq: Int,
    @ColumnInfo(name = "luzMesitaDch") var luzMesitaDch: Int,
    @ColumnInfo(name = "luzOficina") var luzOficina: Int,
    @ColumnInfo(name = "luzGaming") var luzGaming: Int,
    @ColumnInfo(name = "luzR") var luzR: Int,
    @ColumnInfo(name = "luzG") var luzG: Int,
    @ColumnInfo(name = "luzB") var luzB: Int,
    @ColumnInfo(name = "luzGaraje") var luzGaraje: Int,
    @ColumnInfo(name = "luzPorche") var luzPorche: Int,
    @ColumnInfo(name = "luzJardin") var luzJardin: Int,
    @ColumnInfo(name = "luzTendedero") var luzTendedero: Int,

    // SEGURIDAD
    @ColumnInfo(name = "segInt") var segInt: Int,
    @ColumnInfo(name = "segExt") var segExt: Int,
    @PrimaryKey(autoGenerate = true) val id: Int = 0 // First Column
)