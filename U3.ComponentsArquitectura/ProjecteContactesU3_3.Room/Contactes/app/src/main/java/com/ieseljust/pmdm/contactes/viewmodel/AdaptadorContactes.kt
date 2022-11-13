package com.ieseljust.pmdm.contactes.viewmodel

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ieseljust.pmdm.contactes.R
import com.ieseljust.pmdm.contactes.model.db.Contacte
import com.ieseljust.pmdm.contactes.repository.ContacteRepository

// L'adaptador rebrà en el seu constructor els objectes d'escolta
// d'esdeveniments per al clic i el clic llarg
class AdaptadorContactes(
    val eventListenerClick: (Contacte, View) -> Unit,
    val eventListenerLongClick: (Contacte, View) -> Boolean,
    val viewModel: AppContactesViewModel
): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // S'invoca quan es crea un nou viewholder
        val inflater = LayoutInflater.from(parent.context)
        val vista=inflater.inflate(R.layout.contacte_materialcardview_layout, parent,false)
        return ContacteViewHolder(vista)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // Hem de proporcionar-li el contacte,
        // i els esdeveniments de clic i clic llarg que rebem
        // El contacte l'obtenim ara a través de la llista de contactes del ViewModel
            (holder as ContacteViewHolder).bind(
                viewModel.contacteList.value?.get(position) as Contacte,
                eventListenerClick,
                eventListenerLongClick)
    }

    // Utilitzem també la llista de contactes del ViewModel
    override fun getItemCount(): Int {
        // Retorna el nombre d'elements del Dataset
        Log.d("Num Contactes", viewModel.contacteList.value?.size.toString());

        return   viewModel.contacteList.value?.size ?: -1;
    }
}
