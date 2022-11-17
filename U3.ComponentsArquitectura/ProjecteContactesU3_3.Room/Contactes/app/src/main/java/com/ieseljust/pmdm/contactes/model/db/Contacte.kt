package com.ieseljust.pmdm.contactes.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Contacte(
    @PrimaryKey(autoGenerate = true) var id:Long = 0,
    var name:String,
    var classe: String?,
    var img: Int,
    var phone: String?,
    var email:String?
): Serializable