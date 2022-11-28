package com.ieseljust.pmdm.incidenciesv2.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Incidencia (
    @PrimaryKey(autoGenerate = true) var id:Long = 0,
    var assumpte: String,
    var descripcio: String?,
    var ubicacio: String?,
    var servei: String?, // Jardineria, Infraestructures, Obres
    var img: String?,
    var resolt: Boolean?
): Serializable
