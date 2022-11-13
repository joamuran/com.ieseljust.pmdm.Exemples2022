package com.ieseljust.pmdm.contactes.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Contacte::class], version = 1)
abstract class ContactesDB : RoomDatabase() {
    // Mètode que retorna el DAO que hem implementat
    // I amb el qual accedire a la BD
    abstract fun contacteDao(): ContacteDAO
}

// Implementació del patró Singleton
object DatabaseBuilder {

    private var INSTANCE: ContactesDB? = null

    fun getInstance(context: Context): ContactesDB {
        if (INSTANCE == null) {
            synchronized(ContactesDB::class) {
                INSTANCE = buildRoomDB(context)
            }
        }
        return INSTANCE!!
    }

    // Constructor privat
    private fun buildRoomDB(context: Context) =
        Room.databaseBuilder(
            context.applicationContext,
            ContactesDB::class.java,
            "contactes-db"
        ).build()
}