package com.ieseljust.pmdm.incidenciesv2.model.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface IncidenciaDAO
{

    /* Mètodes de conveniència */

    // Afegir una incidència
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addIncidencia(incidencia: Incidencia)

    // Actualitzar una incidència
    @Update
    suspend fun updateIncidencia(incidencia: Incidencia)

    // Eliminar una incidència
    @Delete
    suspend fun deleteIncidencia(incidencia: Incidencia): Int

    /* Mètodes de cerca: Consultes */

    // Obtindre la llista d'Incidències
    @Query("SELECT * from Incidencia")
    fun getAll(): LiveData<List<Incidencia>>

}