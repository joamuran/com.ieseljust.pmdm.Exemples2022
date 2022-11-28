package com.ieseljust.pmdm.apac2

import java.io.Serializable

data class Incidencia (
    var id:Int,
    var assumpte: String,
    var descripcio: String,
    var ubicacio: String,
    var servei: String, // Jardineria, Infraestructures, Obres
    var img: Int,
    var resolt: Boolean
    ): Serializable
