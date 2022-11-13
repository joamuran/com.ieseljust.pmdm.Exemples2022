package com.ieseljust.pmdm.contactes.view.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class MyDialogFragment(): DialogFragment() {
    private var title="Titol per defecte"
    private var content="Contingut per defecte"

    // Definim el listner (qui invoca el diàleg) com a
    // una propietat més, amb lateinit.
    private lateinit var listener: OkOrCancelDialogable

    constructor (title:String, content:String) : this() {
        this.title=title
        this.content=content

    }

    // Definim la interfície que hauran d'implementar
    // els fragments que vulguen utilitzar el diàleg.
    // Aquesta defineix dos mètodes per a les classes que la
    // implementen, i que es corresponen a les accions del
    // Clic en OK o en Cancel.
    interface OkOrCancelDialogable {
        fun onPositiveClick()
        fun onCancelClick()
    }


    // onAttach: Mètode del cicle de vida que es dispara
    // quan el fragment s'adjunta a l'activitat.
    // En aquest moment, inicialitzem el listener.

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verifiquem que qui ens invoca implementa la interfície
        // per fer possibles els callbacks
        try {
            // Instanciem el diàleg per poder invocar els callbacks
            // parentFragment és el fragment pare (NavHostFragment)
            // des del parent Fragment accedim als fragments del fill
            // i obtenim el que està en la posició 0.
            // Amb això obtenim el fragment que es troba actualment
            // en el host de navegació, que no és altre que FirstFragment.
            listener = parentFragment?.childFragmentManager?.fragments?.get(0) as OkOrCancelDialogable

        } catch (e: ClassCastException) {
            // Si el fragment que ens invoca no implementa la interfície
            // llancem una excepció.
            throw ClassCastException(
                (listener.toString() +
                        " must implement OkOrCancelDialogable")
            )
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        if(savedInstanceState!=null) {
            title=savedInstanceState.getString("title")?:title
            content=savedInstanceState.getString("content")?:content
        }

        return activity?.let {
            val builder: AlertDialog.Builder=AlertDialog
                .Builder(requireActivity())
            // Establim el títol i el contingut del diàleg
            builder.setTitle(title).setMessage(content)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    // Invoquem al callback per al click en acceptar
                    // del listener.
                    listener.onPositiveClick()
                }
                .setNegativeButton(android.R.string.cancel) { _, _ ->
                    // Invoquem al callback per al click en cancel·lar
                    // del listener.
                    listener.onCancelClick()
                }
            // Retornen l'AlertDialog,
            return builder.create()} ?: throw IllegalStateException("El fragment no està associat a cap fragment")
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("title", title)
        outState.putString("content", content)
    }
}
