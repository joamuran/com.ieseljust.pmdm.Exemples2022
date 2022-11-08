package com.ieseljust.pmdm.contactes.repository

import android.content.Context
import android.util.Log
import com.ieseljust.pmdm.contactes.R
import com.ieseljust.pmdm.contactes.model.Contacte
import com.ieseljust.pmdm.contactes.model.Contactes

class ContacteRepository private constructor(
    // Constructor primari privat, de manera que només es podrà
    // invocar des de la pròpia classe.

    // Aquest constructor rebrà un context, que necessitarem
    // per tal d'accedir als recursos.
    private var context: Context
) {

    init {
        // Bloc d'inicialització. Quan es cree la classe,
        // es plenarà de contingut amb el recurs JSON contacts.
        Log.d("Depurant", "Estic per aci...")
        Contactes.inflate(context, R.raw.contacts)

    }

    // Aci ve la màgia del singleton:
    // Creem un objecte complementari (estàtic) que continga la
    // instància única de la classe, i el mètode getInstance, per
    // tal d'obtenir aquesta.
    // Si és la primera vegada que s'invoca (i per tant la instància
    // de la classe no existeix), es crea aquesta instància.
    // Si no és la primera vegada que s'invoca (i per tant la instància
    // sí que existeix), el que fem, simplement és retornar-la.

    companion object {
        private var INSTANCE: ContacteRepository? = null
        fun getInstance(context: Context): ContacteRepository {
            if (INSTANCE == null) {
                INSTANCE = ContacteRepository(context)
            }
            return INSTANCE!!
        }
    }

    // Mètodes que ofereix aquest API del repositori:

    fun getContactes() = Contactes.contactes
    fun getNumContactes() = Contactes.contactes.size
    fun removeContacte(c: Contacte) = Contactes.remove(c)
    fun addContacte(c:Contacte)=Contactes.add(c)
    fun replaceContacte(c1: Contacte, c2: Contacte)=Contactes.replace(c1, c2)

}
