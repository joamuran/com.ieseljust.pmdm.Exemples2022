package com.ieseljust.pmdm.contactes

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

class MyDialogFragment(): DialogFragment() {
    var title="Titol per defecte"
    var content="Contingut per defecte"

        constructor (title:String, content:String) : this() {
        this.title=title
        this.content=content

    }

    // Definim la interfície que hauran d'implementa
    // les activitats que vulguen utilitzar el fragment.
    // Aquesta defineix dos mètodes per a les classes que la
    // implementen, i que es corresponen a les accions del
    // Clic en OK o en Cancel.
    interface OkOrCancelDialogable {
        fun onPositiveClick()
        fun onCancelClick()
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

         title=savedInstanceState?.getString("title")?:title
         content=savedInstanceState?.getString("content")?:content

        return activity?.let {
            val builder: AlertDialog.Builder=AlertDialog
                .Builder(requireActivity())
            // title i content es defineixen com a propietats de la
            // pròpia classe al constructor primari
            builder.setTitle(title).setMessage(content)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    // Obtenim el fragment que ha invocat el diàleg,
                    // mitjançant parentFragment,
                    // i li fem un càsting a la interfície OkOrCancelDialogable

                    // Invoquem al mètode onPositiveClick implementat per
                    // l'activitat que implementa OkOrCancelDialogable

                    val listener=parentFragment as OkOrCancelDialogable?
                    listener!!.onPositiveClick()
                }
                .setNegativeButton(android.R.string.cancel) { _, _ ->
                    val listener=parentFragment as OkOrCancelDialogable?
                    listener!!.onCancelClick()
                }
            // Retorna un AlertDialog,
            // tal com l'hem definit en el Builder
            return builder.create()} ?: throw IllegalStateException("El fragment no està associat a cap fragment")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("title", title)
        outState.putString("content", content)
    }

}