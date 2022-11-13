package com.ieseljust.pmdm.contactes.model.db

import androidx.lifecycle.LiveData
import androidx.room.*

/* Classe d'Accés a Dades */

@Dao
interface ContacteDAO {

    /* Mètodes de conveniència */

    // Afegir un contacte
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addContacte(contacte: Contacte)

    // Actualitzar un contacte
    @Update
    suspend fun updateContacte(contacte: Contacte)

    // Eliminar un contacte
    @Delete
    suspend fun deleteContacte(contacte: Contacte): Int

    /* Mètodes de cerca: Consultes */

    // Obtindre la llista de Contactes
    @Query("SELECT * from Contacte")
    fun getAll(): LiveData<List<Contacte>>


}
