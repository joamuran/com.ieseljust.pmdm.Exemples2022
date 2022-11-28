package com.ieseljust.pmdm.apac2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

// L'adaptador rebrà en el seu constructor els objectes d'escolta
// d'esdeveniments per al clic i el clic llarg
class AdaptadorIncidencies(
    val eventListenerClick: (Incidencia, View) -> Unit,
    val eventListenerLongClick: (Incidencia, View) -> Boolean
): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // S'invoca quan es crea un nou viewholder
        val inflater = LayoutInflater.from(parent.context)
        val vista=inflater.inflate(R.layout.incidencia_layout, parent,false);
        return IncidenciaViewHolder(vista)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // Hem de proporcionar-li el parc,
        // i els esdeveniments de clic i clic llarg que rebem
        (holder as IncidenciaViewHolder).bind(
            Incidencies.incidencies[position],
            eventListenerClick,
            eventListenerLongClick)
    }

    override fun getItemCount(): Int {
        // Retorna el nombre d'elements del Dataset
        return Incidencies.incidencies.size
    }
}