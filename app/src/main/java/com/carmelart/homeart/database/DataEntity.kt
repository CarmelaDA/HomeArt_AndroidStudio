package com.carmelart.homeart.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DataEntity(

    // SEGURIDAD
    @ColumnInfo(name = "segInt") var segInt: Int,
    @ColumnInfo(name = "segExt") var segExt: Int,

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

    // PUERTAS Y VENTANAS
    @ColumnInfo(name = "pParcela") var pParcela: Int,
    @ColumnInfo(name = "pGaraje") var pGaraje: Int,
    @ColumnInfo(name = "vSalon") var vSalon: Int,
    @ColumnInfo(name = "vDormitorio") var vDormitorio: Int,
    @ColumnInfo(name = "vOficina") var vOficina: Int,

    // TIEMPO
    @ColumnInfo(name = "tVentInt") var tVentInt: Int,
    @ColumnInfo(name = "tCalefInt") var tCalefInt: Int,
    @ColumnInfo(name = "tVentExt") var tVentExt: Int,
    @ColumnInfo(name = "tCalefExt") var tCalefExt: Int,

    // EXTERIOR
    @ColumnInfo(name = "tTendedero") var tTendedero: Int,
    //...@ColumnInfo(name = "luzTendedero") var luzTendedero: Int,
    //...@ColumnInfo(name = "luzPorche") var luzPorche: Int,
    //...@ColumnInfo(name = "luzJardin") var luzJardin: Int,
    //...@ColumnInfo(name = "pParcela") var pParcela: Int,

    // SALÓN
    @ColumnInfo(name = "sTelevision") var sTelevision: Int,
    //...@ColumnInfo(name = "luzSala") var luzSala: Int,
    //...@ColumnInfo(name = "luzComedor") var luzComedor: Int,
    //...@ColumnInfo(name = "luzAmbiente") var luzAmbiente: Int,
    //...@ColumnInfo(name = "vSalon") var vSalon: Int,

    // HUERTO
    @ColumnInfo(name = "rHuerto") var rHuerto: Int,

    @PrimaryKey(autoGenerate = true) val id: Int = 0 // First Column
)