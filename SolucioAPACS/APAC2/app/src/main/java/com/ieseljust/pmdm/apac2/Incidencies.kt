package com.ieseljust.pmdm.apac2

import android.content.Context
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.lang.Exception
import java.util.*

object Incidencies {

    // Internament guardem una llista d'incidències
    var incidencies: ArrayList<Incidencia>
    var last_id=0;

    init {
        //  Inicialització de la llista d'incidències
        incidencies = ArrayList<Incidencia>()
    }


    // Mètode per afegir una incidència
    fun add(incidencia:Incidencia):Int{
        last_id++
        incidencies.add(incidencia)
        return last_id-1
    }

    // Mètode per afegir una incidència
    // donades les propietats d'aquesta

    fun add(assumpte: String,
            descripcio: String,
            zona: String,
            servei: String,
            img: Int,
            resolt: Boolean):Int{

        // Afegirà automàticament last_id com a id

        return add(Incidencia(
            last_id,
            assumpte,
            descripcio,
            zona,
            servei,
            img,
            resolt
        ))
    }


    // Métode per modificar una incidencia
    fun update(incidencia:Incidencia){
        // Busquem en la llista d'incidències la incidència
        // amb l'id (aquest camp no es pot modificar)
        for (i in 0 until incidencies.size) {
            Log.d("Debug", "Index: "+i)
            Log.d("Debug", "incidencies[i].id="+incidencies[i].id+" incidencia.id="+incidencia.id)
            if (incidencies[i].id == incidencia.id) {
                Log.d("Debug", "Coincidencia amb index " + i)
                incidencies[i] = incidencia
                return
            }
        }
    }

    // Mètode per eliminar una incidencia
    fun remove(incidenciaAEliminar: Incidencia):Int{
        val index= incidencies.indexOf(incidenciaAEliminar)
        incidencies.remove(incidenciaAEliminar)
        return index // Retorna l'índex de la incidència eliminada
    }

    fun afigDadesInicials(context:Context){
        if (incidencies.size==0) { // Per assegurar-nos que no es cree cada vegada que es gire la pantalla
            val idImatge: Int =
                context.resources.getIdentifier("incidencia", "drawable", context.packageName)

            add(
                "Arbre caigut",
                "Amb les últimes pluges i temporals ha caigut un arbre",
                "Carrer la Barca",
                "Jardineria",
                idImatge,
                false
            )

            add(
                "Vorera trencada",
                "A l'altura del número 50 hi ha una vorera trencada, que ha produït diverses caigudes",
                "Carrer Pintor Sorolla",
                "Obres",
                idImatge,
                false
            )

            add(
                "Tanques al carrer",
                "Després dels concerts del passat mes de setembre, encara queden tanques per recollir, que dificulten el pas als vianants.",
                "Plaça Juan Carlos I",
                "Infraestructures",
                idImatge,
                false
            )
        }
    }
}