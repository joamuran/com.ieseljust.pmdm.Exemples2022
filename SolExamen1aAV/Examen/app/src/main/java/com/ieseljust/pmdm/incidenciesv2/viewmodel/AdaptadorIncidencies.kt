package com.ieseljust.pmdm.apac2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ieseljust.pmdm.incidenciesv2.R
import com.ieseljust.pmdm.incidenciesv2.model.db.Incidencia
import com.ieseljust.pmdm.incidenciesv2.viewmodel.IncidenciesViewModel

// L'adaptador rebrà en el seu constructor els objectes d'escolta
// d'esdeveniments per al clic i el clic llarg
class AdaptadorIncidencies(
    val eventListenerClick: (Incidencia) -> Unit,
    val eventListenerLongClick: (Incidencia) -> Boolean,
    val ratingListener: (Incidencia, Float) -> Unit,
    private val viewModel: IncidenciesViewModel
): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // S'invoca quan es crea un nou viewholder
        val inflater = LayoutInflater.from(parent.context)
        val vista=inflater.inflate(R.layout.incidencia_layout, parent,false);
        return IncidenciaViewHolder(vista)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // Hem de proporcionar-li la incidència
        // i els esdeveniments de clic i clic llarg que rebem
        (holder as IncidenciaViewHolder).bind(
            viewModel.incidenciaList.value?.get(position) as Incidencia,
            eventListenerClick,
            eventListenerLongClick,
            ratingListener)
    }

    override fun getItemCount(): Int {
        // Retorna el nombre d'elements del Dataset
        return   viewModel.incidenciaList.value?.size ?: -1
    }
}