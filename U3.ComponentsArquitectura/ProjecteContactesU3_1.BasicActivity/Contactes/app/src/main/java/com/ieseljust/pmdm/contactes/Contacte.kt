package com.ieseljust.pmdm.contactes

import java.io.Serializable

data class Contacte(
    var name:String,
    var classe: String,
    var img: Int,
    var phone: String,
    var email:String
): Serializable