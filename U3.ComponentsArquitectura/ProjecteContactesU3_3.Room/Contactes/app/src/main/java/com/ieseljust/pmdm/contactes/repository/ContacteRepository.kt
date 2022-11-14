package com.ieseljust.pmdm.contactes.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.ieseljust.pmdm.contactes.model.db.Contacte
import com.ieseljust.pmdm.contactes.model.db.DatabaseBuilder

class ContacteRepository private constructor(
    // Constructor primari privat, de manera que només es podrà
    // invocar des de la pròpia classe.

    // Aquest constructor rebrà un context, que necessitarem
    // per tal d'accedir als recursos.
    private var context: Context
) {

    // Com que ja no anem a inicialitzar els contactes, no
    // necessitem el bloc init.

    //  Aquest mètode queda igual que estava, per implementar
    // la creació del repositori seguint un patró Singleton.

    companion object {
        private var INSTANCE: ContacteRepository? = null
        fun getInstance(context: Context): ContacteRepository {
            if (INSTANCE == null) {
                INSTANCE = ContacteRepository(context)
            }
            return INSTANCE!!
        }
    }

    // I aci venen les modificacions, ja que ara, en lloc d'accedir a
    // l'objecte Contactes, ho fem directament sobre la base de dades.

    // Modificarem també l'API que ofereix aquesta funció, per adaptar-la
    // Més al funcionament CRUD d'una BD.

    fun getContactes(): LiveData<List<Contacte>>{
        return DatabaseBuilder.getInstance(context).contacteDao().getAll()
    }

    suspend fun removeContacte(contacte: Contacte): Int =
        DatabaseBuilder.getInstance(context).contacteDao().deleteContacte(contacte)

    suspend fun updateContacte(contacte:Contacte) =
        DatabaseBuilder.getInstance(context).contacteDao().updateContacte(contacte)

    suspend fun addContacte(contacte:Contacte) =
        DatabaseBuilder.getInstance(context).contacteDao().addContacte(contacte)


}
