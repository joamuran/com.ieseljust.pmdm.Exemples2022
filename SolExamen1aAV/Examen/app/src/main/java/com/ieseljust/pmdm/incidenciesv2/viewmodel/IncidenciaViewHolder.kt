package com.ieseljust.pmdm.apac2


import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.ieseljust.pmdm.incidenciesv2.R
import com.ieseljust.pmdm.incidenciesv2.model.db.Incidencia

class  IncidenciaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val img = itemView.findViewById(R.id.incidenciaImg) as ImageView
    val issue = itemView.findViewById(R.id.textViewIssue) as TextView
    val desc = itemView.findViewById(R.id.textViewDescription) as TextView

    // Adaptació per al RatingBar
    val rating=itemView.findViewById(R.id.ratingBarViewHolder) as RatingBar
    // Fi adaptació

    // Enllacem les dades de la incidencia amb la vista
    // i amb els seus gestors d'esdeveniments, com a funcions Lambda

    fun bind(
        incidencia: Incidencia,
        eventListenerClick: (Incidencia) -> Unit,
        eventListenerLongClick: (Incidencia) -> Boolean,
        ratingListener: (Incidencia, Float) -> Unit        // Per a l'adaptació al Rating
    ) {
        issue.text = incidencia.assumpte
        desc.text=incidencia.descripcio

        // Adaptació per al RatingBar
        if ((incidencia.resolt ?: false) == true) {
            rating.setIsIndicator(false)
            rating.rating=incidencia.valoracio?:0.0f
        } else rating.setIsIndicator(true)
        // Fi adaptació

        // Obtenim l'ID a partir del nom de la imatge
        val imgid=when(incidencia.img) {
            "" -> itemView.context.resources.getIdentifier(
                "incidencia",
                "drawable",
                itemView.context.packageName
            )
            else -> itemView.context.resources.getIdentifier(
                incidencia.img,
                "drawable",
                itemView.context.packageName
            )
        }

        img.setImageResource(imgid)

        Log.d("DebugMe", imgid.toString())

        /* Ara capturem els esdeveniments i invoquem el callback corresponent */

        // Click normal
        itemView.setOnClickListener{
            eventListenerClick(incidencia)
        }

        // Click llarg
        itemView.setOnLongClickListener{
            eventListenerLongClick(incidencia)
        }

        // Adaptació per al RAtingBar
        rating.setOnRatingBarChangeListener({ ratingBar, v, b ->
            ratingListener(incidencia, v)
        })
        // Fi Adaptació

    }

}


