package com.ieseljust.pmdm.contactes.viewmodel

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ieseljust.pmdm.contactes.R
import com.ieseljust.pmdm.contactes.model.db.Contacte

class ContacteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    val img = itemView.findViewById(R.id.imageView2) as ImageView
    val name = itemView.findViewById(R.id.textView) as TextView

    // Enllacem les dades del contacte amb la vista
    // i amb els seus gestors d'esdeveniments, com a funcions Lambda
    fun bind(
        contacte: Contacte,
        eventListenerClick: (Contacte, View) -> Unit,
        eventListenerLongClick: (Contacte, View) -> Boolean
    ) {
        name.text = contacte.name
        img.setImageResource(contacte.img)

        /* Ara capturem els esdeveniments i invoquem el callback corresponent */

        // Click normal
        itemView.setOnClickListener{
            eventListenerClick(contacte, itemView)
            }

        // Click llarg
        itemView.setOnLongClickListener{
            eventListenerLongClick(contacte, itemView)
        }
    }


}




