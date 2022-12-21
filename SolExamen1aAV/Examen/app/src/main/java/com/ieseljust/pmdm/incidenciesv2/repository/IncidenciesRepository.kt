package com.ieseljust.pmdm.incidenciesv2.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.ieseljust.pmdm.incidenciesv2.DatabaseBuilder
import com.ieseljust.pmdm.incidenciesv2.model.db.Incidencia

class IncidenciesRepository private constructor(
    private var context: Context
) {

    // Creació del repositori seguint un patró Singleton.

    companion object {
        private var INSTANCE: IncidenciesRepository? = null
        fun getInstance(context: Context): IncidenciesRepository {
            if (INSTANCE == null) {
                INSTANCE = IncidenciesRepository(context)
            }
            return INSTANCE!!
        }
    }

    // API del repositori

    fun getIncidencies(): LiveData<List<Incidencia>> {
        return DatabaseBuilder.getInstance(context).incidenciaDAO().getAll()
    }

    suspend fun deleteIncidencia(incidencia: Incidencia): Int =
        DatabaseBuilder.getInstance(context).incidenciaDAO().deleteIncidencia(incidencia)

    suspend fun updateIncidencia(incidencia:Incidencia) =
        DatabaseBuilder.getInstance(context).incidenciaDAO().updateIncidencia(incidencia)

    suspend fun addIncidencia(incidencia:Incidencia) =
        DatabaseBuilder.getInstance(context).incidenciaDAO().addIncidencia(incidencia)


}
