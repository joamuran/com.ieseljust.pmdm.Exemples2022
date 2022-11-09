package com.ieseljust.pmdm.contactes.model

import android.content.Context
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.lang.Exception
import java.util.*

// Objecte Contactes: Conté una llista de contactes, representant el model de dades

object Contactes {

    // Internament guardem una llista de contactes
    var contactes: ArrayList<Contacte>

    init {
        //  Inicialització de la llista de contactes
        contactes = ArrayList<Contacte>()
    }

    // Mètode per afegir un contacte
    fun add(contacte: Contacte){
        contactes.add(contacte);
    }

    // Métode per reemplaçar un contacte
    fun replace(original: Contacte, newContacte: Contacte){
        val index= contactes.indexOf(original)
        contactes[index]=newContacte
    }

    // Mètode per eliminar un contacte
    fun remove(contacteAEliminar: Contacte):Int{
        val index= contactes.indexOf(contacteAEliminar)
        contactes.remove(contacteAEliminar)
        return index // Retorna l'índex del contacte eliminat
    }

    /* Mètode inflate: "Unfla" la llista amb el contingut del fitxer res/raw/contactes.json
    *
    * L'estructura d'aquest fitxers guarda el següent esquema:
    *
    *     "contacts": [{
            "name": "Cassian Andor",
            "class": "amigos",
            "img": "andor",
            "phone": "555123456",
            "email": "c.andor@bandorebelde.com"
        },
    *   ...
    *  ]
    *
    * */

    fun inflate(context: Context, resource: Int) {
        // Requereix d'un context per tal d'accedir als recursos, i de l'identificador del recurs


        // Si ja te informació, no l'afegirem de nou
        if (contactes.size>0) return

        // Per tal de llegir el recurs, en primer lloc obtindrem un inputStream
        // a partir del recurs JSON indicar i llegim el fitxer
        // Per tal d'accedir a aquest recurs, necessitarem l'id i el context,
        // des d'on podem accedir al component resources.
        val inputStream = context.resources.openRawResource(resource)

        // En segon lloc, llegim de l'stream. Amb el delimitador  \\A
        // indiquem que es llisca tot el fitxer, i obtenim un String amb
        // tot el contingut del JSON
        val jsonString: String = Scanner(inputStream).useDelimiter("\\A").next()

        // Convertim l'string a JSON (JSONObject) amb el següent codi:
        val obj = (JSONTokener(jsonString)).nextValue() as JSONObject

        // Obtenim el JSONArray del componente "contactes"
        val jsa = obj["contacts"] as JSONArray

        // Recorrem el JSONArray, creant els contactes i afegint-los a l'ArrayList

        for (i in 0 until jsa.length()) {
            var contacte = jsa[i] as JSONObject

            // Obtenim l'ID de recurs a partir del nom
            // de la imatge
            val rscName=contacte["img"] as String
            val id:Int=context.resources.getIdentifier(rscName, "drawable", context.packageName)

            try {
                contactes.add(
                    Contacte(
                        contacte["name"] as String,
                        contacte["classe"] as String,
                        id,
                        contacte["phone"] as String,
                        contacte["email"] as String
                    )
                )
            }
            catch(e:Exception){
                Log.e("[Contactes.kt]", e.toString())
            }
        }

    } // End inflate


}


