package com.ieseljust.pmdm.incidenciesv2

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ieseljust.pmdm.incidenciesv2.model.db.Incidencia
import com.ieseljust.pmdm.incidenciesv2.model.db.IncidenciaDAO

@Database(entities = [Incidencia::class], version = 2)
abstract class IncidenciesDB: RoomDatabase() {
    // Mètode que retorna el DAO que hem implementat
    // I amb el qual accedire a la BD
    abstract fun incidenciaDAO(): IncidenciaDAO
}

// Implementació del patró Singleton
object DatabaseBuilder {

    private var INSTANCE: IncidenciesDB? = null

    // Migració de l'esquema, de la versió 1 a la 2
    val MIGRATION_1_2: Migration = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                "ALTER TABLE Incidencia "
                        + "ADD COLUMN valoracio REAL"
            )
        }
    }

    fun getInstance(context: Context): IncidenciesDB {
        if (INSTANCE == null) {
            synchronized(IncidenciesDB::class) {
                INSTANCE = buildRoomDB(context)
            }
        }
        return INSTANCE!!
    }

    // Constructor privat
    private fun buildRoomDB(context: Context) =
        Room.databaseBuilder(
            context.applicationContext,
            IncidenciesDB::class.java,
            "incidencies-db"
        ).addMigrations(MIGRATION_1_2).build()
}