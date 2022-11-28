package com.ieseljust.pmdm.apac2


import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class  IncidenciaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val img = itemView.findViewById(R.id.incidenciaImg) as ImageView
    val issue = itemView.findViewById(R.id.textViewIssue) as TextView
    val desc = itemView.findViewById(R.id.textViewDescription) as TextView

    // Enllacem les dades de la incidencia amb la vista
    // i amb els seus gestors d'esdeveniments, com a funcions Lambda

    fun bind(
        incidencia: Incidencia,
        eventListenerClick: (Incidencia, View) -> Unit,
        eventListenerLongClick: (Incidencia, View) -> Boolean
    ) {
        issue.text = incidencia.assumpte
        desc.text=incidencia.descripcio
        img.setImageResource(incidencia.img)

        /* Ara capturem els esdeveniments i invoquem el callback corresponent */

        // Click normal
        itemView.setOnClickListener{
            eventListenerClick(incidencia, itemView)
        }

        // Click llarg
        itemView.setOnLongClickListener{
            eventListenerLongClick(incidencia, itemView)
        }
    }

}




